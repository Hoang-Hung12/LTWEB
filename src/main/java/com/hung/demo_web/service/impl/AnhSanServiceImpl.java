package com.hung.demo_web.service.impl;
import com.hung.demo_web.dto.AnhSanDto;
import com.hung.demo_web.entity.AnhSan;
import com.hung.demo_web.repository.AnhSanRepository;
import com.hung.demo_web.service.AnhSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnhSanServiceImpl implements AnhSanService {
    @Autowired private AnhSanRepository anhSanRepository;

    private AnhSanDto mapToDTO(AnhSan entity) {
        AnhSanDto dto = new AnhSanDto();
        dto.setMaAnh(entity.getMaAnh());
        dto.setDuongDanAnh(entity.getDuongDanAnh());
        if(entity.getMaSan() != null) dto.setMaSan(entity.getMaSan().getMaSan());
        return dto;
    }

    @Override
    public List<AnhSanDto> getAnhByMaSan(String maSan) {
        return anhSanRepository.findByMaSan_MaSan(maSan).stream().map(this::mapToDTO).collect(Collectors.toList());
    }
}