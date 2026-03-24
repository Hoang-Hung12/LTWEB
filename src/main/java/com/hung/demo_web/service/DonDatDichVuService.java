package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.DonDatDichVuDto;

public interface DonDatDichVuService {
    List<DonDatDichVuDto>  getDichVuDonDat(String maDon);
    DonDatDichVuDto themDichVuVaoDon(DonDatDichVuDto dto);
}
