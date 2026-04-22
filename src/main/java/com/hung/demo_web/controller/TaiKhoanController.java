package com.hung.demo_web.controller;

import com.hung.demo_web.dto.TaiKhoanDto;
import com.hung.demo_web.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taikhoan")
@CrossOrigin("*")
public class TaiKhoanController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    /** Lấy toàn bộ danh sách tài khoản (Admin) */
    @GetMapping
    public ResponseEntity<List<TaiKhoanDto>> getAll() {
        return ResponseEntity.ok(taiKhoanService.getAllTaiKhoan());
    }

    /** Lấy tài khoản theo mã TK */
    @GetMapping("/{maTK}")
    public ResponseEntity<TaiKhoanDto> getById(@PathVariable String maTK) {
        return ResponseEntity.ok(taiKhoanService.getById(maTK));
    }

    /** Lấy tài khoản theo số điện thoại */
    @GetMapping("/sdt/{sdt}")
    public ResponseEntity<TaiKhoanDto> getBySdt(@PathVariable String sdt) {
        return ResponseEntity.ok(taiKhoanService.getBySdt(sdt));
    }

    /** Đăng ký tài khoản mới */
    @PostMapping("/register")
    public ResponseEntity<TaiKhoanDto> register(@RequestBody TaiKhoanDto taiKhoanDto) {
        return new ResponseEntity<>(taiKhoanService.createTaiKhoan(taiKhoanDto), HttpStatus.CREATED);
    }

    /** Đăng nhập — trả về thông tin user đầy đủ */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TaiKhoanDto loginRequest) {
        if (loginRequest == null || loginRequest.getSdt() == null || loginRequest.getMatKhau() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Thiếu số điện thoại hoặc mật khẩu");
        }
        boolean isSuccess = taiKhoanService.login(loginRequest.getSdt(), loginRequest.getMatKhau());
        if (isSuccess) {
            TaiKhoanDto user = taiKhoanService.getBySdt(loginRequest.getSdt());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai số điện thoại hoặc mật khẩu");
        }
    }

    /** Cập nhật thông tin tài khoản (Admin hoặc chính chủ) */
    @PutMapping("/{maTK}")
    public ResponseEntity<TaiKhoanDto> update(@PathVariable String maTK, @RequestBody TaiKhoanDto dto) {
        return ResponseEntity.ok(taiKhoanService.updateTaiKhoan(maTK, dto));
    }

    /** Xóa tài khoản (Admin) */
    @DeleteMapping("/{maTK}")
    public ResponseEntity<Void> delete(@PathVariable String maTK) {
        taiKhoanService.deleteTaiKhoan(maTK);
        return ResponseEntity.noContent().build();
    }

    /** Bước 1 — Gửi OTP về email khi quên mật khẩu */
    @PostMapping("/quen-mat-khau")
    public ResponseEntity<String> quenMatKhau(@RequestParam String email) {
        taiKhoanService.guiOtpQuenMatKhau(email);
        return ResponseEntity.ok("Mã OTP đã được gửi đến email " + email + ". Có hiệu lực trong 5 phút.");
    }

    /** Bước 2 — Xác nhận OTP và đặt lại mật khẩu mới */
    @PostMapping("/dat-lai-mat-khau")
    public ResponseEntity<String> datLaiMatKhau(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String matKhauMoi) {
        taiKhoanService.datLaiMatKhau(email, otp, matKhauMoi);
        return ResponseEntity.ok("Đặt lại mật khẩu thành công!");
    }
}
