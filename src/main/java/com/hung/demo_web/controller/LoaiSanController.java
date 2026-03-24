package com.hung.demo_web.controller;

import com.hung.demo_web.dto.LoaiSanDto;
import com.hung.demo_web.service.LoaiSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loaisan")
@CrossOrigin("*")
public class LoaiSanController {

    @Autowired
    private LoaiSanService loaiSanService;

    @GetMapping
    public ResponseEntity<List<LoaiSanDto>> getAll() {
        return ResponseEntity.ok(loaiSanService.getAllLoaiSan());
    }

    @GetMapping("/{maLoaiSan}")
    public ResponseEntity<LoaiSanDto> getById(@PathVariable String maLoaiSan) {
        return ResponseEntity.ok(loaiSanService.getLoaiSanById(maLoaiSan));
    }
}