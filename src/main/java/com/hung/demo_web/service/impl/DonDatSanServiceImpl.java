package com.hung.demo_web.service.impl;
import com.hung.demo_web.dto.DonDatSanDto;
import com.hung.demo_web.entity.*;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.*;
import com.hung.demo_web.service.DonDatSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonDatSanServiceImpl implements DonDatSanService {
    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private SanRepository sanRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;
    @Autowired private KhuyenMaiRepository khuyenMaiRepository;

    private DonDatSanDto mapToDTO(DonDatSan entity) {
        DonDatSanDto dto = new DonDatSanDto();
        dto.setMaDon(entity.getMaDon());
        dto.setNgayDat(entity.getNgayDat());
        dto.setNgayDa(entity.getNgayDa());
        dto.setGioBatDau(entity.getGioBatDau());
        dto.setGioKetThuc(entity.getGioKetThuc());
        dto.setTienSan(entity.getTienSan());
        dto.setTienCoc(entity.getTienCoc());
        dto.setTrangThai(entity.getTrangThai());
        dto.setMaKH(entity.getKhachHang().getMaTK());
        dto.setTenKhachHang(entity.getKhachHang().getHoTen());
        dto.setMaSan(entity.getSan().getMaSan());
        dto.setTenSan(entity.getSan().getTenSan());
        if (entity.getKhuyenMai() != null) dto.setMaCodeKhuyenMai(entity.getKhuyenMai().getMaCode());
        return dto;
    }

    @Override
    public List<DonDatSanDto> getAllDonDat() {
        return donDatSanRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<DonDatSanDto> getLichSuDatSan(String maKH) {
        return donDatSanRepository.findByKhachHang_maTK(maKH).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
@Override
    @org.springframework.transaction.annotation.Transactional // Fix lỗi 8: Race condition
    public DonDatSanDto taoDonDatSan(DonDatSanDto dto) {
        if (dto.getGioBatDau().isAfter(dto.getGioKetThuc()) || dto.getGioBatDau().equals(dto.getGioKetThuc())) {
            throw new LoiThaoTac("Giờ bắt đầu phải nhỏ hơn giờ kết thúc!");
        }
        TaiKhoan khach = taiKhoanRepository.findById(dto.getMaKH()).orElseThrow(() -> new KhongTimThay("Không tìm thấy Khách hàng"));
        San san = sanRepository.findById(dto.getMaSan()).orElseThrow(() -> new KhongTimThay("Không tìm thấy Sân bóng"));
        
        List<DonDatSan> cacDonCungNgay = donDatSanRepository.findBySan_MaSanAndNgayDa(san.getMaSan(), dto.getNgayDa());
        for (DonDatSan donCu : cacDonCungNgay) {
            if (!donCu.getTrangThai().equals("Đã hủy")) {
                boolean biTrungGio = (dto.getGioBatDau().isBefore(donCu.getGioKetThuc())) && (dto.getGioKetThuc().isAfter(donCu.getGioBatDau()));
                if (biTrungGio) throw new LoiThaoTac("Sân này đã có người đặt giờ đó!");
            }
        }
        
        long soPhutDa = java.time.Duration.between(dto.getGioBatDau(), dto.getGioKetThuc()).toMinutes();
        double tongTienSan = Math.round((san.getGiaThue() / 90.0) * soPhutDa);
        
        DonDatSan entity = new DonDatSan();
        
        // Fix lỗi 3: Server tự sinh ID, không tin client
        entity.setMaDon((dto.getMaDon() != null && !dto.getMaDon().isEmpty()) ? dto.getMaDon() : "ORD" + System.currentTimeMillis()); 

        // Fix lỗi 2: Validate khuyến mãi và lưu vào đơn
        if (dto.getMaCodeKhuyenMai() != null && !dto.getMaCodeKhuyenMai().isEmpty()) {
            KhuyenMai km = khuyenMaiRepository.findByMaCode(dto.getMaCodeKhuyenMai()).orElseThrow(() -> new LoiThaoTac("Mã không tồn tại"));
            if (km.getTrangThai() == 0 || km.getNgayHetHan().isBefore(java.time.LocalDate.now())) {
                throw new LoiThaoTac("Mã khuyến mãi đã hết hạn hoặc bị khóa!");
            }
            if (km.getLoaiKhuyenMai().equalsIgnoreCase("PhanTram")) tongTienSan -= (tongTienSan * (km.getGiaTri() / 100.0));
            else if (km.getLoaiKhuyenMai().equalsIgnoreCase("SoTien")) tongTienSan -= km.getGiaTri();
            
            entity.setKhuyenMai(km); // Rất quan trọng: Lưu lại khóa ngoại
        }

        // Fix lỗi 5: Xử lý trừ DiemSuDung của khách
        if (dto.getDiemSuDung() > 0) {
            if (khach.getDiemTichLuy() < dto.getDiemSuDung()) throw new LoiThaoTac("Điểm tích lũy không đủ!");
            tongTienSan -= dto.getDiemSuDung() * 100; // Giả sử 1 điểm = 100đ
            khach.setDiemTichLuy(khach.getDiemTichLuy() - dto.getDiemSuDung());
            taiKhoanRepository.save(khach);
            entity.setDiemSuDung(dto.getDiemSuDung());
        } else {
            entity.setDiemSuDung(0);
        }

        if (tongTienSan < 0) tongTienSan = 0;

        entity.setNgayDa(dto.getNgayDa());
        entity.setGioBatDau(dto.getGioBatDau());
        entity.setGioKetThuc(dto.getGioKetThuc());
        entity.setTrangThai("Chờ duyệt");
        entity.setKhachHang(khach);
        entity.setSan(san);
        entity.setTienSan(tongTienSan);
        entity.setTienCoc(tongTienSan * 0.3);
        entity.setDiemThuong((int) (tongTienSan / 10000));
        
        return mapToDTO(donDatSanRepository.save(entity));
    }

    // Fix lỗi 7: Hàm cập nhật trạng thái đơn (Dành cho Admin duyệt/hủy)
    @Override
    @org.springframework.transaction.annotation.Transactional
    public DonDatSanDto capNhatTrangThai(String maDon, String trangThai) {
        DonDatSan entity = donDatSanRepository.findById(maDon).orElseThrow(() -> new KhongTimThay("Không tìm thấy đơn"));
        entity.setTrangThai(trangThai);
        return mapToDTO(donDatSanRepository.save(entity));
    }
}