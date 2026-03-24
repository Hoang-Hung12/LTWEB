package com.hung.demo_web.controller;

import com.hung.demo_web.dto.HoaDonDto;
import com.hung.demo_web.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hoadon")
@CrossOrigin("*")
public class HoaDonController {

    @Autowired
    private HoaDonService hoaDonService;

    // API xuất hóa đơn: Truyền mã Đơn và mã Admin xử lý
    @PostMapping("/xuat")
    public ResponseEntity<HoaDonDto> xuatHoaDon(
            @RequestParam String maDon, 
            @RequestParam String maQLLap) {
        return new ResponseEntity<>(hoaDonService.xuatHoaDon(maDon, maQLLap), HttpStatus.CREATED);
    }
}