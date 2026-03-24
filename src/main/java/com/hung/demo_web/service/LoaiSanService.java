package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.LoaiSanDto;

public interface LoaiSanService {
    List<LoaiSanDto> getAllLoaiSan();
    LoaiSanDto getLoaiSanById(String maLoaiSan);
}
