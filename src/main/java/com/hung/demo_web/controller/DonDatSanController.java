package com.hung.demo_web.controller;

import com.hung.demo_web.dto.DonDatSanDto;
import com.hung.demo_web.service.DonDatSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dondatsan")
@CrossOrigin("*")
public class DonDatSanController {

    @Autowired
    private DonDatSanService donDatSanService;

    @GetMapping
    public ResponseEntity<List<DonDatSanDto>> getAll() {
        return ResponseEntity.ok(donDatSanService.getAllDonDat());
    }

    @GetMapping("/lich-su/{maKH}")
    public ResponseEntity<List<DonDatSanDto>> getLichSu(@PathVariable String maKH) {
        return ResponseEntity.ok(donDatSanService.getLichSuDatSan(maKH));
    }

    @PostMapping
    public ResponseEntity<DonDatSanDto> createDon(@RequestBody DonDatSanDto dto) {
        // Nhận JSON từ Frontend, đẩy xuống Service để kiểm tra logic và tính tiền
        return new ResponseEntity<>(donDatSanService.taoDonDatSan(dto), HttpStatus.CREATED);
    }
}