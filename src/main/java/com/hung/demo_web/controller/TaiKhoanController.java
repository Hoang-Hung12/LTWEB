package com.hung.demo_web.controller;

import com.hung.demo_web.dto.TaiKhoanDto;
import com.hung.demo_web.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taikhoan") // Đường dẫn gốc cho API tài khoản
@CrossOrigin("*") // Cho phép Frontend ở cổng khác gọi vào không bị lỗi CORS
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
}