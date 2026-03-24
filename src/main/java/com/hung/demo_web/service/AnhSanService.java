package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.AnhSanDto;

public interface AnhSanService {
    List<AnhSanDto> getAnhByMaSan(String maSan);
}
