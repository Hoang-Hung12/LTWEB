package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.KhuyenMaiDto;

public interface KhuyenMaiService {
    List<KhuyenMaiDto> getAllKhuyenMai();
    KhuyenMaiDto kiemTraMaKhuyenMai(String macode);    
}
