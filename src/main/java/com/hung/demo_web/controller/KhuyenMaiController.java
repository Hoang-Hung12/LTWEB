package com.hung.demo_web.controller;

import com.hung.demo_web.dto.KhuyenMaiDto;
import com.hung.demo_web.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khuyenmai")
@CrossOrigin("*")
public class KhuyenMaiController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    @GetMapping
    public ResponseEntity<List<KhuyenMaiDto>> getAll() {
        return ResponseEntity.ok(khuyenMaiService.getAllKhuyenMai());
    }

    // API này dùng khi khách bấm nút "Áp dụng mã" trên giao diện
    @GetMapping("/check/{maCode}")
    public ResponseEntity<KhuyenMaiDto> checkCode(@PathVariable String maCode) {
        return ResponseEntity.ok(khuyenMaiService.kiemTraMaKhuyenMai(maCode));
    }
}