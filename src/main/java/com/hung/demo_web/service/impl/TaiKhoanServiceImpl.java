package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.TaiKhoanDto;
import com.hung.demo_web.entity.DonDatSan;
import com.hung.demo_web.entity.TaiKhoan;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.mail.MailService;
import com.hung.demo_web.repository.DonDatDichVuRepository;
import com.hung.demo_web.repository.DonDatSanRepository;
import com.hung.demo_web.repository.HoaDonRepository;
import com.hung.demo_web.repository.TaiKhoanRepository;
import com.hung.demo_web.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired private TaiKhoanRepository repo;
    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private DonDatDichVuRepository donDatDichVuRepository;
    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private MailService mailService;

    // Lưu OTP tạm thời: key = email, value = [otp, thoiGianHetHan]
    private final Map<String, String[]> otpStore = new ConcurrentHashMap<>();

    private TaiKhoanDto mapToDTO(TaiKhoan entity) {
        TaiKhoanDto dto = new TaiKhoanDto();
        dto.setMaTK(entity.getMaTK());
        dto.setHoTen(entity.getHoTen());
        dto.setSdt(entity.getSdt());
        dto.setEmail(entity.getEmail());
        dto.setVaiTro(entity.getVaiTro());
        dto.setDiemTichLuy(entity.getDiemTichLuy());
        dto.setHangThanhVien(entity.getHangThanhVien());
        dto.setNgayTao(entity.getNgayTao());
        return dto;
    }

    @Override
    public List<TaiKhoanDto> getAllTaiKhoan() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public TaiKhoanDto getBySdt(String sdt) {
        TaiKhoan entity = repo.findBySdt(sdt)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy số điện thoại: " + sdt));
        return mapToDTO(entity);
    }

    @Override
    public TaiKhoanDto getById(String maTK) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));
        return mapToDTO(entity);
    }

    @Override
    public TaiKhoanDto createTaiKhoan(TaiKhoanDto dto) {
        if (repo.findBySdt(dto.getSdt()).isPresent()) {
            throw new LoiThaoTac("Số điện thoại đã được đăng ký!");
        }
        TaiKhoan entity = new TaiKhoan();
        entity.setMaTK(dto.getMaTK() != null ? dto.getMaTK() : "TK" + System.currentTimeMillis());
        entity.setHoTen(dto.getHoTen());
        entity.setSdt(dto.getSdt());
        entity.setEmail(dto.getEmail());
        entity.setMatKhau(dto.getMatKhau());
        entity.setVaiTro("USER");
        entity.setDiemTichLuy(0);
        entity.setHangThanhVien("Đồng");
        return mapToDTO(repo.save(entity));
    }

    @Override
    public TaiKhoanDto updateTaiKhoan(String maTK, TaiKhoanDto dto) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));
        if (dto.getHoTen() != null) entity.setHoTen(dto.getHoTen());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getSdt() != null) entity.setSdt(dto.getSdt());
        if (dto.getMatKhau() != null && !dto.getMatKhau().isEmpty()) {
            entity.setMatKhau(dto.getMatKhau());
        }
        if (dto.getDiemTichLuy() != null) {
            entity.setDiemTichLuy(dto.getDiemTichLuy());
            int diem = dto.getDiemTichLuy();
            if (diem >= 1000) entity.setHangThanhVien("Vàng");
            else if (diem >= 500) entity.setHangThanhVien("Bạc");
            else entity.setHangThanhVien("Đồng");
        }
        if (dto.getHangThanhVien() != null) entity.setHangThanhVien(dto.getHangThanhVien());
        return mapToDTO(repo.save(entity));
    }

    @Override
    @Transactional
    public void deleteTaiKhoan(String maTK) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));

        // Chỉ chặn xóa khi khách còn đơn đặt sân đang xử lý (không phải Đã hoàn thành / Đã hủy)
        var allDon = donDatSanRepository.findByKhachHang_maTK(maTK);
        long activeDon = allDon.stream()
                .filter(d -> !"Đã hủy".equalsIgnoreCase(d.getTrangThai()) && !"Đã hoàn thành".equalsIgnoreCase(d.getTrangThai()))
                .count();
        if (activeDon > 0) {
            throw new LoiThaoTac(
                "Không thể xóa khách hàng \"" + entity.getHoTen() + "\" vì đang có " + activeDon +
                " đơn đặt sân đang hoạt động. Vui lòng hoàn thành hoặc hủy những đơn này trước khi xóa khách hàng."
            );
        }

        // Nếu chỉ còn các đơn đã hoàn thành / đã hủy, xóa các dữ liệu liên quan trước.
        for (DonDatSan don : allDon) {
            if (don.getMaDon() != null) {
                hoaDonRepository.deleteByDonDatSan_MaDon(don.getMaDon());
                donDatDichVuRepository.deleteById_maDon(don.getMaDon());
            }
        }
        if (!allDon.isEmpty()) {
            donDatSanRepository.deleteAll(allDon);
        }

        repo.delete(entity);
    }

    @Override
    public boolean login(String sdt, String matKhau) {
        if (sdt == null || matKhau == null) return false;
        String s = sdt.trim();
        String m = matKhau.trim();
        if (s.isEmpty() || m.isEmpty()) return false;
        
        var result = repo.findBySdt(s);
        if (result.isPresent()) {
            TaiKhoan tk = result.get();
            String db = tk.getMatKhau();
            return db != null && db.trim().equals(m);
        } else {
            return false;
        }
    }

    @Override
    public void guiOtpQuenMatKhau(String email) {
        // Tìm tài khoản theo email
        TaiKhoan tk = repo.findByEmail(email)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản với email: " + email));

        // Sinh OTP 6 chữ số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Lưu vào store: [otp, thời điểm hết hạn = bây giờ + 5 phút]
        String hetHan = LocalDateTime.now().plusMinutes(5).toString();
        otpStore.put(email, new String[]{otp, hetHan});

        // Gửi mail (async — không block response)
        mailService.sendMailOtpQuenMatKhau(tk.getHoTen(), otp, email);
    }

    @Override
    public void datLaiMatKhau(String email, String otp, String matKhauMoi) {
        // Kiểm tra OTP có trong store không
        String[] data = otpStore.get(email);
        if (data == null) {
            throw new LoiThaoTac("Mã OTP không hợp lệ hoặc chưa được gửi.");
        }

        // Kiểm tra hết hạn
        LocalDateTime hetHan = LocalDateTime.parse(data[1]);
        if (LocalDateTime.now().isAfter(hetHan)) {
            otpStore.remove(email);
            throw new LoiThaoTac("Mã OTP đã hết hạn. Vui lòng yêu cầu gửi lại.");
        }

        // Kiểm tra OTP có khớp không
        if (!data[0].equals(otp)) {
            throw new LoiThaoTac("Mã OTP không chính xác.");
        }

        // Tất cả hợp lệ → cập nhật mật khẩu mới
        TaiKhoan tk = repo.findByEmail(email)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản."));
        tk.setMatKhau(matKhauMoi.trim());
        repo.save(tk);

        // Xóa OTP khỏi store sau khi dùng xong
        otpStore.remove(email);
    }
}
