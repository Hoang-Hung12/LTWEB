package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.SanDto;

public interface SanService {
    List<SanDto> getAllSan();
    SanDto getSanById(String maSan);
    SanDto createSan(SanDto sanDto);
    SanDto updateSan(String maSan, SanDto sanDto);
    void deleteSan(String maSan);
}
