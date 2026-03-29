package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.HoaDonDto;
import com.hung.demo_web.entity.*;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.*;
import com.hung.demo_web.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private DonDatDichVuRepository donDatDichVuRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;

    private HoaDonDto mapToDTO(HoaDonEntity entity) {
        HoaDonDto dto = new HoaDonDto();
        dto.setMaHoaDon(entity.getMaHoaDon());
        dto.setTongTienDichVu(entity.getTongTienDichVu());
        dto.setTongThanhToan(entity.getTongThanhToan());
        dto.setNgayLap(entity.getNgayLap());
        if (entity.getDonDatSan() != null) {
            dto.setMaDon(entity.getDonDatSan().getMaDon());
        }
        if (entity.getNguoiLap() != null) {
            dto.setMaQLLap(entity.getNguoiLap().getMaTK());
            dto.setTenQLLap(entity.getNguoiLap().getHoTen());
        }
        return dto;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public HoaDonDto xuatHoaDon(String maDon, String maQLLap) {

        // Chống tạo hóa đơn trùng
        if (hoaDonRepository.existsByDonDatSan_MaDon(maDon)) {
            throw new LoiThaoTac("Đơn này đã có hóa đơn rồi!");
        }

        DonDatSan donDat = donDatSanRepository.findById(maDon)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Đơn đặt sân"));
        TaiKhoan admin = taiKhoanRepository.findById(maQLLap)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Admin"));

        if (!donDat.getTrangThai().equals("Hoàn thành")) {
            throw new LoiThaoTac("Đơn đặt sân chưa hoàn thành, không thể xuất hóa đơn!");
        }

        // Tính tổng tiền dịch vụ
        List<DonDat_DichVu> listDichVu = donDatDichVuRepository.findById_maDon(maDon);
        double tongTienDV = 0;
        for (DonDat_DichVu dv : listDichVu) {
            tongTienDV += dv.getThanhTien();
        }

        // Tổng thanh toán = Tiền sân + Tiền DV - Tiền cọc đã đặt trước
        double tienCoc = donDat.getTienCoc() != null ? donDat.getTienCoc() : 0;
        double tongThanhToan = donDat.getTienSan() + tongTienDV - tienCoc;
        if (tongThanhToan < 0) tongThanhToan = 0;

        // SỬA: Thêm kiểm tra null trước khi cộng điểm để tránh NullPointerException
        // Lý do: cột DiemTichLuy và DiemThuong trong DB có thể là NULL
        // → getDiemTichLuy() + getDiemThuong() = null + null → crash
        TaiKhoan khach = donDat.getKhachHang();
        int diemHienTai = khach.getDiemTichLuy() != null ? khach.getDiemTichLuy() : 0;
        int diemThuong  = donDat.getDiemThuong()  != null ? donDat.getDiemThuong()  : 0;
        int tongDiem    = diemHienTai + diemThuong;

        khach.setDiemTichLuy(tongDiem);

        // SỬA: Thêm logic tự động cập nhật HangThanhVien sau khi cộng điểm
        // Lý do: code cũ chỉ cộng điểm nhưng không bao giờ nâng hạng → sai nghiệp vụ
        if      (tongDiem >= 500) khach.setHangThanhVien("Kim Cương");
        else if (tongDiem >= 200) khach.setHangThanhVien("Vàng");
        else if (tongDiem >= 100) khach.setHangThanhVien("Bạc");
        else                      khach.setHangThanhVien("Đồng");

        taiKhoanRepository.save(khach);

        // Cập nhật trạng thái đơn → Đã thanh toán
        donDat.setTrangThai("Đã thanh toán");
        donDatSanRepository.save(donDat);

        // Tạo hóa đơn
        HoaDonEntity hoaDon = new HoaDonEntity();
        hoaDon.setMaHoaDon("HD" + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        hoaDon.setDonDatSan(donDat);
        hoaDon.setNguoiLap(admin);
        hoaDon.setTongTienDichVu(tongTienDV);
        hoaDon.setTongThanhToan(tongThanhToan);

        return mapToDTO(hoaDonRepository.save(hoaDon));
    }
}