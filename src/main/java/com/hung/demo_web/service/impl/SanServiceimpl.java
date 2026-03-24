package com.hung.demo_web.service.impl;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hung.demo_web.dto.SanDto;
import com.hung.demo_web.entity.San;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.SanRepository;
import com.hung.demo_web.service.SanService;
@Service
public class SanServiceimpl implements SanService{
    @Autowired private SanRepository sanRepository;
    private SanDto mapToDto(San entity){
        SanDto dto = new SanDto();
        dto.setMaSan(entity.getMaSan());
        dto.setTenSan(entity.getTenSan());
        dto.setDiaChi(entity.getDiaChi());
        dto.setMoTa(entity.getMoTa());
        dto.setAnhChinh(entity.getAnhChinh());
        dto.setTienIch(entity.getTienIch());
        dto.setGiaThue(entity.getGiaThue());
        dto.setTrangThai(entity.getTrangThai());
        if(entity.getLoaiSan()!=null){
            dto.setTenLoaiSan(entity.getLoaiSan().getTenLoaiSan());
            dto.setSoNguoi(entity.getLoaiSan().getSoNguoi());
        }
        return dto;
    }
    @Override
    public List<SanDto>  getAllSan(){
        return sanRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }
    @Override
    public SanDto getSanById(String maSan){
        San entity = sanRepository.findById(maSan).orElseThrow(()-> new KhongTimThay("Không tìm thấy sân"+maSan));
        return mapToDto(entity);
    }
    @Override
    public SanDto createSan(SanDto sanDto){
        San entity = new San();
        entity.setMaSan(sanDto.getMaSan());
        entity.seTenSan(sanDto.getTenSan());
        entity.setDiaChi(sanDto.getDiachi());
        entity.setMota(sanDto.getMoTa());
        entity.setTienIch(sanDto.getTienIch());
        entity.setAnhChinh(sanDto.getAnhChinh());
        entity.setGiaThue(sanDto.getGiaThue());
        entity.setTrangThai(sanDto.getTrangThai());
        return mapToDto(sanRepository.save(entity));
    }
}
