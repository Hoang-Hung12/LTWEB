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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DonDatSanServiceImpl implements DonDatSanService {

    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private SanRepository sanRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;
    @Autowired private KhuyenMaiRepository khuyenMaiRepository;

    private DonDatSanDto mapToDTO(DonDatSan entity, boolean includeProof) {
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
        dto.setSdtKhachHang(entity.getKhachHang().getSdt());
        dto.setMaSan(entity.getSan().getMaSan());
        dto.setTenSan(entity.getSan().getTenSan());
        if (entity.getKhuyenMai() != null) {
            dto.setMaCodeKhuyenMai(entity.getKhuyenMai().getMaCode());
        }
        dto.setPhuongThucThanhToan(entity.getPhuongThucThanhToan());
        dto.setDaThanhToanCoc(Boolean.TRUE.equals(entity.getDaXacNhanThanhToan()));
        dto.setChungTuThanhToan(includeProof ? entity.getChungTuThanhToan() : null);
        dto.setMaAdminXacNhanThanhToan(entity.getMaAdminXacNhanThanhToan());
        dto.setThoiGianXacNhanThanhToan(entity.getThoiGianXacNhanThanhToan());
        return dto;
    }

    @Override
    public List<DonDatSanDto> getAllDonDat() {
        return donDatSanRepository.findAll().stream().map(e -> mapToDTO(e, false)).collect(Collectors.toList());
    }

    @Override
    public List<DonDatSanDto> getLichSuDatSan(String maKH) {
        return donDatSanRepository.findByKhachHang_maTK(maKH).stream().map(e -> mapToDTO(e, false)).collect(Collectors.toList());
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public DonDatSanDto taoDonDatSan(DonDatSanDto dto) {
        String pttt = dto.getPhuongThucThanhToan() == null ? "" : dto.getPhuongThucThanhToan().trim().toUpperCase();
        if (!pttt.equals("TIEN_MAT") && !pttt.equals("CHUYEN_KHOAN")) {
            throw new LoiThaoTac("Vui lòng chọn phương thức thanh toán hợp lệ!");
        }
        if (!Boolean.TRUE.equals(dto.getDaThanhToanCoc())) {
            throw new LoiThaoTac("Cần thanh toán cọc trước khi đặt sân!");
        }

        // Validate giờ
        if (dto.getGioBatDau().isAfter(dto.getGioKetThuc()) || dto.getGioBatDau().equals(dto.getGioKetThuc())) {
            throw new LoiThaoTac("Giờ bắt đầu phải nhỏ hơn giờ kết thúc!");
        }

        TaiKhoan khach = taiKhoanRepository.findById(Objects.requireNonNull(dto.getMaKH()))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Khách hàng"));
        San san = sanRepository.findById(Objects.requireNonNull(dto.getMaSan()))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Sân bóng"));

        // Kiểm tra trùng giờ
        List<DonDatSan> cacDonCungNgay = donDatSanRepository.findBySan_MaSanAndNgayDa(san.getMaSan(), dto.getNgayDa());
        for (DonDatSan donCu : cacDonCungNgay) {
            if (!donCu.getTrangThai().equals("Đã hủy")) {
                boolean biTrungGio = dto.getGioBatDau().isBefore(donCu.getGioKetThuc())
                        && dto.getGioKetThuc().isAfter(donCu.getGioBatDau());
                if (biTrungGio) throw new LoiThaoTac("Sân này đã có người đặt giờ đó!");
            }
        }

        // Tính tiền theo số phút thực tế (chuẩn: giá thuê / 90 phút)
        long soPhut = java.time.Duration.between(dto.getGioBatDau(), dto.getGioKetThuc()).toMinutes();
        double tongTienSan = Math.round((san.getGiaThue() / 90.0) * soPhut);

        DonDatSan entity = new DonDatSan();
        entity.setMaDon((dto.getMaDon() != null && !dto.getMaDon().isEmpty())
                ? dto.getMaDon()
                : String.format("ORD%03d", donDatSanRepository.count() + 1));

        // Áp dụng khuyến mãi (nếu có)
        if (dto.getMaCodeKhuyenMai() != null && !dto.getMaCodeKhuyenMai().isEmpty()) {
            KhuyenMai km = khuyenMaiRepository.findByMaCode(dto.getMaCodeKhuyenMai())
                    .orElseThrow(() -> new LoiThaoTac("Mã không tồn tại"));
            if (km.getTrangThai() == 0 || km.getNgayHetHan().isBefore(java.time.LocalDate.now())) {
                throw new LoiThaoTac("Mã khuyến mãi đã hết hạn hoặc bị khóa!");
            }
            validatePromoByMemberLevel(km, khach);
            if (km.getLoaiKhuyenMai().equalsIgnoreCase("PhanTram")) {
                tongTienSan -= tongTienSan * (km.getGiaTri() / 100.0);
            } else if (km.getLoaiKhuyenMai().equalsIgnoreCase("SoTien")) {
                tongTienSan -= km.getGiaTri();
            }
            entity.setKhuyenMai(km);
        }

        // SỬA: Thêm kiểm tra null trước khi so sánh để tránh NullPointerException
        // Lý do: client có thể không gửi field diemSuDung → dto.getDiemSuDung() = null
        // → null > 0 → crash ngay lập tức
        if (dto.getDiemSuDung() != null && dto.getDiemSuDung() > 0) {
            if (khach.getDiemTichLuy() < dto.getDiemSuDung()) {
                throw new LoiThaoTac("Điểm tích lũy không đủ!");
            }
            tongTienSan -= dto.getDiemSuDung() * 100; // 1 điểm = 100đ
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
        entity.setPhuongThucThanhToan(pttt);
        entity.setDaXacNhanThanhToan(false);
        entity.setChungTuThanhToan(dto.getChungTuThanhToan());
        entity.setKhachHang(khach);
        entity.setSan(san);
        entity.setTienSan(tongTienSan);
        entity.setTienCoc(tongTienSan * 0.3);
        entity.setDiemThuong((int) (tongTienSan / 10000));

        return mapToDTO(donDatSanRepository.save(entity), false);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public DonDatSanDto capNhatTrangThai(String maDon, String trangThai) {
        DonDatSan entity = donDatSanRepository.findById(Objects.requireNonNull(maDon))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy đơn"));
        if ("Chờ duyệt".equalsIgnoreCase(trangThai) && !Boolean.TRUE.equals(entity.getDaXacNhanThanhToan())) {
            entity.setDaXacNhanThanhToan(true);
        }
        entity.setTrangThai(trangThai);
        return mapToDTO(donDatSanRepository.save(entity), false);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public DonDatSanDto xacNhanThanhToan(String maDon, String maAdmin) {
        DonDatSan entity = donDatSanRepository.findById(Objects.requireNonNull(maDon))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy đơn"));
        if (!"Chờ xác nhận thanh toán".equals(entity.getTrangThai())) {
            throw new LoiThaoTac("Đơn không ở trạng thái chờ xác nhận thanh toán.");
        }
        entity.setDaXacNhanThanhToan(true);
        entity.setTrangThai("Chờ duyệt");
        entity.setMaAdminXacNhanThanhToan(maAdmin);
        entity.setThoiGianXacNhanThanhToan(new java.sql.Timestamp(System.currentTimeMillis()));
        return mapToDTO(donDatSanRepository.save(entity), false);
    }

    @Override
    public String layChungTuThanhToan(String maDon) {
        DonDatSan entity = donDatSanRepository.findById(Objects.requireNonNull(maDon))
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy đơn"));
        return entity.getChungTuThanhToan();
    }

    private void validatePromoByMemberLevel(KhuyenMai km, TaiKhoan khach) {
        String code = km.getMaCode() == null ? "" : km.getMaCode().toUpperCase();
        String rank = khach.getHangThanhVien() == null ? "" : khach.getHangThanhVien().toUpperCase();
        int diem = khach.getDiemTichLuy() == null ? 0 : khach.getDiemTichLuy();

        // Mã DONG: chỉ dành cho hạng Đồng (dưới 500 điểm)
        if (code.startsWith("DONG") && diem >= 500) {
            throw new LoiThaoTac("Mã này chỉ áp dụng cho hạng Đồng (dưới 500 điểm tích lũy).");
        }
        // Mã BAC: chỉ dành cho hạng Bạc (500–999 điểm)
        if (code.startsWith("BAC") && (diem < 500 || diem >= 1000)) {
            throw new LoiThaoTac("Mã này chỉ áp dụng cho hạng Bạc (500–999 điểm tích lũy).");
        }
        // Mã VANG: chỉ dành cho hạng Vàng
        if (code.startsWith("VANG") && !rank.contains("VÀNG")) {
            throw new LoiThaoTac("Mã này chỉ áp dụng cho hạng Vàng.");
        }
        // Mã VIP: yêu cầu tối thiểu 1000 điểm (tương đương hạng Vàng)
        if (code.startsWith("VIP") && diem < 1000) {
            throw new LoiThaoTac("Mã này yêu cầu tối thiểu 1000 điểm tích lũy.");
        }
    }
}