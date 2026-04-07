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
import java.util.Objects;

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

        DonDatSan donDat = donDatSanRepository.findById(Objects.requireNonNull(maDon))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Đơn đặt sân"));
        TaiKhoan admin = taiKhoanRepository.findById(Objects.requireNonNull(maQLLap))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Admin"));

        if (!"Đã xác nhận".equals(donDat.getTrangThai()) && !"Đã hoàn thành".equals(donDat.getTrangThai())) {
            throw new LoiThaoTac("Đơn đặt sân chưa sẵn sàng để xuất hóa đơn!");
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

        // Điểm thưởng đã được cộng ngay tại thời điểm đặt sân.
        TaiKhoan khach = donDat.getKhachHang();
        int diemHienTai = khach.getDiemTichLuy() == null ? 0 : khach.getDiemTichLuy();
        int diemThuong = donDat.getDiemThuong() == null ? 0 : donDat.getDiemThuong();
        int tongDiem = diemHienTai + diemThuong;
        khach.setDiemTichLuy(tongDiem);
        if (tongDiem >= 1000) khach.setHangThanhVien("Vàng");
        else if (tongDiem >= 500) khach.setHangThanhVien("Bạc");
        else khach.setHangThanhVien("Đồng");
        taiKhoanRepository.save(khach);

        donDat.setTrangThai("Đã hoàn thành");
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