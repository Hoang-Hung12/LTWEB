package com.hung.demo_web.service.impl;
import com.hung.demo_web.dto.KhuyenMaiDto;
import com.hung.demo_web.entity.KhuyenMai;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.KhuyenMaiRepository;
import com.hung.demo_web.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {
    @Autowired private KhuyenMaiRepository khuyenMaiRepository;

    private KhuyenMaiDto mapToDTO(KhuyenMai entity) {
        KhuyenMaiDto dto = new KhuyenMaiDto();
        dto.setMaKM(entity.getMaKM());
        dto.setMaCode(entity.getMaCode());
        dto.setLoaiKhuyenMai(entity.getLoaiKhuyenMai());
        dto.setGiaTri(entity.getGiaTri());
        dto.setNgayHetHan(entity.getNgayHetHan());
        dto.setTrangThai(entity.getTrangThai());
        return dto;
    }

    @Override
    public List<KhuyenMaiDto> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public KhuyenMaiDto kiemTraMaKhuyenMai(String maCode) {
        KhuyenMai entity = khuyenMaiRepository.findByMaCode(maCode)
                .orElseThrow(() -> new KhongTimThay("Mã khuyến mãi không tồn tại!"));
        if (entity.getTrangThai() == 0 || entity.getNgayHetHan().isBefore(LocalDate.now())) {
            throw new LoiThaoTac("Mã khuyến mãi đã hết hạn hoặc bị khóa!");
        }
        return mapToDTO(entity);
    }
}