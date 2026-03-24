package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.DichVuDto;

public interface DichVuService {
    List<DichVuDto> getAll();
    DichVuDto getById(String id);    
}
