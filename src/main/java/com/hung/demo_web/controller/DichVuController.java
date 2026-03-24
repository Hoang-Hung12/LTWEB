package com.hung.demo_web.controller;

import com.hung.demo_web.dto.DichVuDto;
import com.hung.demo_web.service.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dichvu")
@CrossOrigin("*")
public class DichVuController {

    @Autowired
    private DichVuService dichVuService;

    @GetMapping
    public ResponseEntity<List<DichVuDto>> getAll() {
        return ResponseEntity.ok(dichVuService.getAll());
    }

    @GetMapping("/{maDV}")
    public ResponseEntity<DichVuDto> getById(@PathVariable String maDV) {
        return ResponseEntity.ok(dichVuService.getById(maDV));
    }
}