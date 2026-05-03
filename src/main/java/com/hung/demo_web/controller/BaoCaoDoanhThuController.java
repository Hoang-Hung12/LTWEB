package com.hung.demo_web.controller;

import com.hung.demo_web.dto.BaoCaoDoanhThuDto;
import com.hung.demo_web.service.BaoCaoDoanhThuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/baocao")
@CrossOrigin("*")
public class BaoCaoDoanhThuController {

    @Autowired
    private BaoCaoDoanhThuService baoCaoDoanhThuService;

    /**
     * Tổng quan doanh thu
     * Ví dụ:
     *   GET /api/baocao/doanh-thu/tong-quan              → tất cả thời gian
     *   GET /api/baocao/doanh-thu/tong-quan?nam=2025     → cả năm 2025
     *   GET /api/baocao/doanh-thu/tong-quan?thang=4&nam=2025 → tháng 4/2025
     */
    @GetMapping("/doanh-thu/tong-quan")
    public ResponseEntity<BaoCaoDoanhThuDto> tongQuan(
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam) {
        return ResponseEntity.ok(baoCaoDoanhThuService.tongQuan(thang, nam));
    }

    /**
     * Doanh thu từng tháng trong năm (12 tháng)
     * Ví dụ:
     *   GET /api/baocao/doanh-thu/theo-thang?nam=2025
     */
    @GetMapping("/doanh-thu/theo-thang")
    public ResponseEntity<BaoCaoDoanhThuDto> theoThang(
            @RequestParam(required = false) Integer nam) {
        return ResponseEntity.ok(baoCaoDoanhThuService.theoThang(nam));
    }

    /**
     * Doanh thu từng sân (sắp xếp giảm dần)
     * Ví dụ:
     *   GET /api/baocao/doanh-thu/theo-san?thang=4&nam=2025
     *   GET /api/baocao/doanh-thu/theo-san?nam=2025
     */
    @GetMapping("/doanh-thu/theo-san")
    public ResponseEntity<BaoCaoDoanhThuDto> theoSan(
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam) {
        return ResponseEntity.ok(baoCaoDoanhThuService.theoSan(thang, nam));
    }

    /**
     * Top khách hàng chi tiêu nhiều nhất
     * Ví dụ:
     *   GET /api/baocao/doanh-thu/top-khach              → top 10, tất cả thời gian
     *   GET /api/baocao/doanh-thu/top-khach?topN=5&nam=2025
     */
    @GetMapping("/doanh-thu/top-khach")
    public ResponseEntity<BaoCaoDoanhThuDto> topKhach(
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam,
            @RequestParam(required = false, defaultValue = "10") Integer topN) {
        return ResponseEntity.ok(baoCaoDoanhThuService.topKhachHang(thang, nam, topN));
    }
}