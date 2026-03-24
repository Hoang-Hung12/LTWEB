package com.hung.demo_web.controller;

import com.hung.demo_web.dto.DonDatDichVuDto;
import com.hung.demo_web.service.DonDatDichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dondat-dichvu")
@CrossOrigin("*")
public class DonDatDichVuController {

    @Autowired
    private DonDatDichVuService donDatDichVuService;

    @GetMapping("/don/{maDon}")
    public ResponseEntity<List<DonDatDichVuDto>> getByDon(@PathVariable String maDon) {
        return ResponseEntity.ok(donDatDichVuService.getDichVuDonDat(maDon));
    }

    @PostMapping
    public ResponseEntity<DonDatDichVuDto> addDichVu(@RequestBody DonDatDichVuDto dto) {
        return new ResponseEntity<>(donDatDichVuService.themDichVuVaoDon(dto), HttpStatus.CREATED);
    }
}