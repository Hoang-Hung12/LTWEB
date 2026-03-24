package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.DonDatDichVuDto; // Nếu file DTO của bạn đuôi là Dto thì sửa lại chữ O thành o nhé
import com.hung.demo_web.entity.DichVu;       // Tên Entity 1 của bạn
import com.hung.demo_web.entity.DonDatSan;    // Tên Entity 2 của bạn
import com.hung.demo_web.entity.DonDat_DichVu; // Tên Entity chính xác của bạn
import com.hung.demo_web.entity.DonDatDichVuId; // Tên Class ID của bạn

import com.hung.demo_web.exception.KhongTimThay; // Lỗi tiếng Việt bạn đã tạo
import com.hung.demo_web.repository.DichVuRepository;
import com.hung.demo_web.repository.DonDatDichVuRepository;
import com.hung.demo_web.repository.DonDatSanRepository;
import com.hung.demo_web.service.DonDatDichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonDatDichVuServiceImpl implements DonDatDichVuService {

    @Autowired private DonDatDichVuRepository donDatDichVuRepository;
    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private DichVuRepository dichVuRepository;

    // 1. NHẬN VÀO ĐÚNG ENTITY 'DonDat_DichVu' CỦA BẠN
    private DonDatDichVuDto mapToDTO(DonDat_DichVu entity) {
        DonDatDichVuDto dto = new DonDatDichVuDto();
        
        // Gọi đúng hàm getMaDon() và getMaDV() (chữ M viết hoa) từ file ID của bạn
        dto.setMaDon(entity.getId().getMaDon());
        dto.setMaDV(entity.getId().getMaDV());
        
        dto.setSoLuong(entity.getSoLuong());
        dto.setThanhTien(entity.getThanhTien());
        dto.setTenDichVu(entity.getDichVu().getTenDichVu());
        dto.setDonGia(entity.getDichVu().getDonGia());
        return dto;
    }

    @Override
    public List<DonDatDichVuDto> getDichVuDonDat(String maDon) {
        // Nhớ đảm bảo trong DonDatDichVuRepository đã có hàm findById_MaDon nhé
        return donDatDichVuRepository.findById_maDon(maDon).stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public DonDatDichVuDto themDichVuVaoDon(DonDatDichVuDto dto) {
        // 2. TÌM THEO ĐÚNG TÊN CLASS 'DonDatSan' VÀ 'DichVu'
        DonDatSan donDat = donDatSanRepository.findById(dto.getMaDon())
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Đơn đặt sân"));
        DichVu dichVu = dichVuRepository.findById(dto.getMaDV())
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Dịch vụ"));

        // 3. KHỞI TẠO ĐÚNG CLASS ID 'DonDatDichVuId'
        DonDatDichVuId id = new DonDatDichVuId();
        id.setMaDon(dto.getMaDon());
        id.setMaDV(dto.getMaDV());

        // 4. KHỞI TẠO ĐÚNG ENTITY 'DonDat_DichVu'
        DonDat_DichVu entity = new DonDat_DichVu();
        entity.setId(id); 
        
        entity.setDonDatSan(donDat);
        entity.setDichVu(dichVu);
        entity.setSoLuong(dto.getSoLuong());
        entity.setThanhTien(dto.getSoLuong() * dichVu.getDonGia());

        return mapToDTO(donDatDichVuRepository.save(entity));
    }
}