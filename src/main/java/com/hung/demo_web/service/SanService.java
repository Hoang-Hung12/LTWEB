package com.hung.demo_web.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.hung.demo_web.dto.SanDto;

public interface SanService {
    List<SanDto> getAllSan();
    SanDto getSanById(String maSan);
    SanDto createSan(SanDto sanDto);
    SanDto updateSan(String maSan, SanDto sanDto);
    void deleteSan(String maSan);
    /** Tìm sân còn trống theo ngày đá, giờ bắt đầu, giờ kết thúc và loại sân */
    List<SanDto> timSanTrong(LocalDate ngayDa, LocalTime gioBatDau, LocalTime gioKetThuc, String maLoaiSan);
}
