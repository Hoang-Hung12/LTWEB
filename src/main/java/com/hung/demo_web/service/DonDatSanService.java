package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.DonDatSanDto;

public interface DonDatSanService {
    List<DonDatSanDto> getAllDonDat();
    List<DonDatSanDto> getLichSuDatSan(String maKH);
    DonDatSanDto taoDonDatSan(DonDatSanDto dto);
}
