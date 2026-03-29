package com.hung.demo_web.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hung.demo_web.dto.SanDto;
import com.hung.demo_web.entity.LoaiSan;
import com.hung.demo_web.entity.San;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.repository.LoaiSanRepository;
import com.hung.demo_web.repository.SanRepository;
import com.hung.demo_web.service.SanService;

@Service
public class SanServiceimpl implements SanService {

    @Autowired private SanRepository sanRepository;
    @Autowired private LoaiSanRepository loaiSanRepository;

    private SanDto mapToDto(San entity) {
        SanDto dto = new SanDto();
        dto.setMaSan(entity.getMaSan());
        dto.setTenSan(entity.getTenSan());
        dto.setDiaChi(entity.getDiaChi());
        dto.setMoTa(entity.getMoTa());
        dto.setAnhChinh(entity.getAnhChinh());
        dto.setTienIch(entity.getTienIch());
        dto.setGiaThue(entity.getGiaThue());
        dto.setTrangThai(entity.getTrangThai());
        if (entity.getLoaiSan() != null) {
            dto.setMaLoaiSan(entity.getLoaiSan().getMaLoaiSan());
            dto.setTenLoaiSan(entity.getLoaiSan().getTenLoaiSan());
            dto.setSoNguoi(entity.getLoaiSan().getSoNguoi());
        }
        return dto;
    }

    @Override
    public List<SanDto> getAllSan() {
        return sanRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public SanDto getSanById(String maSan) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));
        return mapToDto(entity);
    }

    @Override
    public SanDto createSan(SanDto sanDto) {
        San entity = new San();
        entity.setMaSan(sanDto.getMaSan() != null ? sanDto.getMaSan() : "S" + System.currentTimeMillis());
        // SỬA: seTenSan → setTenSan | setMota → setMoTa (khớp với San.java đã sửa)
        entity.setTenSan(sanDto.getTenSan());
        entity.setDiaChi(sanDto.getDiaChi());
        entity.setMoTa(sanDto.getMoTa());
        entity.setTienIch(sanDto.getTienIch());
        entity.setAnhChinh(sanDto.getAnhChinh());
        entity.setGiaThue(sanDto.getGiaThue());
        entity.setTrangThai(sanDto.getTrangThai());

        if (sanDto.getMaLoaiSan() != null && !sanDto.getMaLoaiSan().isEmpty()) {
            LoaiSan loaiSan = loaiSanRepository.findById(sanDto.getMaLoaiSan())
                    .orElseThrow(() -> new KhongTimThay("Không tìm thấy loại sân"));
            entity.setLoaiSan(loaiSan);
        } else {
            throw new LoiThaoTac("Không được để trống Mã Loại Sân!");
        }
        return mapToDto(sanRepository.save(entity));
    }

    @Override
    public SanDto updateSan(String maSan, SanDto sanDto) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));
        entity.setTenSan(sanDto.getTenSan());
        entity.setDiaChi(sanDto.getDiaChi());
        entity.setMoTa(sanDto.getMoTa());
        entity.setTienIch(sanDto.getTienIch());
        entity.setAnhChinh(sanDto.getAnhChinh());
        entity.setGiaThue(sanDto.getGiaThue());
        entity.setTrangThai(sanDto.getTrangThai());

        if (sanDto.getMaLoaiSan() != null && !sanDto.getMaLoaiSan().isEmpty()) {
            LoaiSan loaiSan = loaiSanRepository.findById(sanDto.getMaLoaiSan())
                    .orElseThrow(() -> new KhongTimThay("Không tìm thấy loại sân"));
            entity.setLoaiSan(loaiSan);
        }
        return mapToDto(sanRepository.save(entity));
    }

    @Override
    public void deleteSan(String maSan) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));
        sanRepository.delete(entity);
    }
}