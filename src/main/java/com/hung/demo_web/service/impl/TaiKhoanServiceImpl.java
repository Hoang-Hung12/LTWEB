package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.TaiKhoanDto;
import com.hung.demo_web.entity.TaiKhoan;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.TaiKhoanRepository;
import com.hung.demo_web.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    @Autowired private TaiKhoanRepository repo;

    private TaiKhoanDto mapToDTO(TaiKhoan entity) {
        TaiKhoanDto dto = new TaiKhoanDto();
        dto.setMaTK(entity.getMaTK());
        dto.setHoTen(entity.getHoTen());
        dto.setSdt(entity.getSdt()); // SỬA: getsdt() → getSdt()
        dto.setEmail(entity.getEmail());
        dto.setVaiTro(entity.getVaiTro());
        dto.setDiemTichLuy(entity.getDiemTichLuy());
        dto.setHangThanhVien(entity.getHangThanhVien());
        return dto;
    }

    @Override
    public List<TaiKhoanDto> getAllTaiKhoan() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public TaiKhoanDto getBySdt(String sdt) {
        TaiKhoan entity = repo.findBySdt(sdt)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy số điện thoại!"));
        return mapToDTO(entity);
    }

    @Override
    public TaiKhoanDto createTaiKhoan(TaiKhoanDto dto) {
        TaiKhoan entity = new TaiKhoan();
        entity.setMaTK(dto.getMaTK() != null ? dto.getMaTK() : "TK" + System.currentTimeMillis());
        entity.setHoTen(dto.getHoTen());
        entity.setSdt(dto.getSdt()); // SỬA: setsdt() → setSdt()
        entity.setEmail(dto.getEmail());
        entity.setMatKhau(dto.getMatKhau());
        entity.setVaiTro("USER");
        entity.setDiemTichLuy(0);
        entity.setHangThanhVien("Đồng");
        return mapToDTO(repo.save(entity));
    }

    @Override
    public boolean login(String sdt, String matKhau) {
        Optional<TaiKhoan> tk = repo.findBySdt(sdt);
        if (tk.isPresent()) {
            return tk.get().getMatKhau().equals(matKhau);
        }
        return false;
    }
}