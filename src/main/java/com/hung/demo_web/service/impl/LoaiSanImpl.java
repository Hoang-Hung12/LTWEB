package com.hung.demo_web.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hung.demo_web.dto.LoaiSanDto;
import com.hung.demo_web.entity.LoaiSan;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.LoaiSanRepository;
import com.hung.demo_web.service.LoaiSanService;

@Service
public class LoaiSanImpl implements LoaiSanService{
    @Autowired private LoaiSanRepository loaiSanRepository;
    private LoaiSanDto mapToDto(LoaiSan entity){
        LoaiSanDto dto = new LoaiSanDto();
        dto.setMaLoaiSan(entity.getMaLoaiSan());
        dto.setTenLoaiSan(entity.getTenLoaiSan());
        dto.setSoNguoi(entity.getSoNguoi());
        return dto;
    }
    @Override
    public List<LoaiSanDto> getAllLoaiSan(){
        return loaiSanRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }
    @Override
    public LoaiSanDto getLoaiSanById(String maLoaiSan){
        LoaiSan entity = loaiSanRepository.findById(maLoaiSan).orElseThrow(()->new KhongTimThay("Không tìm thấy loại sân"));
        return mapToDto(entity);
    }
}
