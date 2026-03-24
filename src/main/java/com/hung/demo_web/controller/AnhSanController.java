package com.hung.demo_web.controller;

import com.hung.demo_web.dto.AnhSanDto;
import com.hung.demo_web.service.AnhSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anhsan")
@CrossOrigin("*")
public class AnhSanController {

    @Autowired
    private AnhSanService anhSanService;

    // API này dùng để Frontend lấy danh sách ảnh của 1 sân làm slide
    @GetMapping("/san/{maSan}")
    public ResponseEntity<List<AnhSanDto>> getAnhBySan(@PathVariable String maSan) {
        return ResponseEntity.ok(anhSanService.getAnhByMaSan(maSan));
    }
}