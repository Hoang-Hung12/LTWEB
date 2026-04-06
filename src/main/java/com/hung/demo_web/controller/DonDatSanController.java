package com.hung.demo_web.controller;

import com.hung.demo_web.dto.DonDatSanDto;
import com.hung.demo_web.entity.DonDatSan;
import com.hung.demo_web.repository.DonDatSanRepository;
import com.hung.demo_web.service.DonDatSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/dondatsan")
@CrossOrigin("*")
public class DonDatSanController {

    @Autowired
    private DonDatSanService donDatSanService;

    @Autowired
    private DonDatSanRepository donDatSanRepository;

    @GetMapping
    public ResponseEntity<List<DonDatSanDto>> getAll() {
        return ResponseEntity.ok(donDatSanService.getAllDonDat());
    }

    @GetMapping("/lich-su/{maKH}")
    public ResponseEntity<List<DonDatSanDto>> getLichSu(@PathVariable String maKH) {
        return ResponseEntity.ok(donDatSanService.getLichSuDatSan(maKH));
    }

    /**
     * Lấy danh sách khung giờ đã được đặt của 1 sân trong 1 ngày.
     * GET /api/dondatsan/gio-da-dat/{maSan}?ngayDa=2026-04-10
     * Trả về: [ {"gioBatDau":"17:00","gioKetThuc":"18:30"}, ... ]
     */
    @GetMapping("/gio-da-dat/{maSan}")
    public ResponseEntity<List<Map<String, String>>> getGioDaDat(
            @PathVariable String maSan,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDa) {
        List<DonDatSan> donList = donDatSanRepository.findBySan_MaSanAndNgayDa(maSan, ngayDa);
        List<Map<String, String>> result = new ArrayList<>();
        for (DonDatSan don : donList) {
            if (!"Đã hủy".equalsIgnoreCase(don.getTrangThai())) {
                Map<String, String> slot = new HashMap<>();
                slot.put("gioBatDau", don.getGioBatDau().toString());
                slot.put("gioKetThuc", don.getGioKetThuc().toString());
                slot.put("trangThai", don.getTrangThai());
                result.add(slot);
            }
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<DonDatSanDto> createDon(@RequestBody DonDatSanDto dto) {
        return new ResponseEntity<>(donDatSanService.taoDonDatSan(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{maDon}/trang-thai")
    public ResponseEntity<DonDatSanDto> capNhatTrangThai(
            @PathVariable String maDon,
            @RequestParam String trangThai) {
        return ResponseEntity.ok(donDatSanService.capNhatTrangThai(maDon, trangThai));
    }
}
