package com.hung.demo_web.service.impl;
import com.hung.demo_web.dto.DichVuDto;
import com.hung.demo_web.entity.DichVu;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.DichVuRepository;
import com.hung.demo_web.service.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DichVuServiceImpl implements DichVuService {
    @Autowired private DichVuRepository repo;

    private DichVuDto mapToDTO(DichVu entity) {
        DichVuDto dto = new DichVuDto();
        dto.setMaDV(entity.getMaDV());
        dto.setTenDichVu(entity.getTenDichVu());
        dto.setDonGia(entity.getDonGia());
        dto.setDonViTinh(entity.getDonViTinh());
        return dto;
    }

    @Override
    public List<DichVuDto> getAll() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public DichVuDto getById(String id) {
        DichVu entity = repo.findById(id).orElseThrow(() -> new KhongTimThay("Không tìm thấy dịch vụ"));
        return mapToDTO(entity);
    }
}