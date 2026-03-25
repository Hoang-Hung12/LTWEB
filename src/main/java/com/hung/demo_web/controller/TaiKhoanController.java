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

    @GetMapping
    public ResponseEntity<List<TaiKhoanDto>> getAll() {
        return ResponseEntity.ok(taiKhoanService.getAllTaiKhoan());
    }

    @GetMapping("/sdt/{sdt}")
    public ResponseEntity<TaiKhoanDto> getBySdt(@PathVariable String sdt) {
        return ResponseEntity.ok(taiKhoanService.getBySdt(sdt));
    }

    @PostMapping("/register")
    public ResponseEntity<TaiKhoanDto> register(@RequestBody TaiKhoanDto taiKhoanDto) {
        return new ResponseEntity<>(taiKhoanService.createTaiKhoan(taiKhoanDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody TaiKhoanDto loginRequest) {
        boolean isSuccess = taiKhoanService.login(loginRequest.getSdt(), loginRequest.getMatKhau());
        if(isSuccess) {
            // Sửa: Trả về Object DTO chứa thông tin user thay vì String
            TaiKhoanDto user = taiKhoanService.getBySdt(loginRequest.getSdt());
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai số điện thoại hoặc mật khẩu");
        }
    }
}