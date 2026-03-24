package com.hung.demo_web.controller;

import com.hung.demo_web.dto.SanDto;
import com.hung.demo_web.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/san")
@CrossOrigin("*")
public class SanController {

    @Autowired
    private SanService sanService;

    @GetMapping
    public ResponseEntity<List<SanDto>> getAll() {
        return ResponseEntity.ok(sanService.getAllSan());
    }

    @GetMapping("/{maSan}")
    public ResponseEntity<SanDto> getById(@PathVariable String maSan) {
        return ResponseEntity.ok(sanService.getSanById(maSan));
    }

    @PostMapping
    public ResponseEntity<SanDto> create(@RequestBody SanDto sanDTO) {
        return new ResponseEntity<>(sanService.createSan(sanDTO), HttpStatus.CREATED);
    }
}