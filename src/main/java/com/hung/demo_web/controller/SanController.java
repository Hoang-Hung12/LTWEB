package com.hung.demo_web.controller;

import com.hung.demo_web.dto.SanDto;
import com.hung.demo_web.service.SanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
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

    /**
     * Tìm sân còn trống theo ngày, giờ, loại sân
     * GET /api/san/tim-kiem?ngayDa=2026-04-10&gioBatDau=17:00&gioKetThuc=18:30&maLoaiSan=LS01
     */
    @GetMapping("/tim-kiem")
    public ResponseEntity<List<SanDto>> timSanTrong(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDa,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime gioBatDau,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime gioKetThuc,
            @RequestParam(required = false) String maLoaiSan) {
        return ResponseEntity.ok(sanService.timSanTrong(ngayDa, gioBatDau, gioKetThuc, maLoaiSan));
    }

    @PostMapping
    public ResponseEntity<SanDto> create(@RequestBody SanDto sanDTO) {
        return new ResponseEntity<>(sanService.createSan(sanDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{maSan}")
    public ResponseEntity<SanDto> update(@PathVariable String maSan, @RequestBody SanDto sanDTO) {
        return ResponseEntity.ok(sanService.updateSan(maSan, sanDTO));
    }

    @DeleteMapping("/{maSan}")
    public ResponseEntity<Void> delete(@PathVariable String maSan) {
        sanService.deleteSan(maSan);
        return ResponseEntity.noContent().build();
    }
}
