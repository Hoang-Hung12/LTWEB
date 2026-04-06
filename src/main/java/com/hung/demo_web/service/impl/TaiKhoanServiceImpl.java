package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.TaiKhoanDto;
import com.hung.demo_web.entity.TaiKhoan;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.exception.LoiThaoTac;
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
        dto.setSdt(entity.getSdt());
        dto.setEmail(entity.getEmail());
        dto.setVaiTro(entity.getVaiTro());
        dto.setDiemTichLuy(entity.getDiemTichLuy());
        dto.setHangThanhVien(entity.getHangThanhVien());
        dto.setNgayTao(entity.getNgayTao());
        return dto;
    }

    @Override
    public List<TaiKhoanDto> getAllTaiKhoan() {
        return repo.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public TaiKhoanDto getBySdt(String sdt) {
        TaiKhoan entity = repo.findBySdt(sdt)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy số điện thoại: " + sdt));
        return mapToDTO(entity);
    }

    @Override
    public TaiKhoanDto getById(String maTK) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));
        return mapToDTO(entity);
    }

    @Override
    public TaiKhoanDto createTaiKhoan(TaiKhoanDto dto) {
        // Kiểm tra số điện thoại đã tồn tại chưa
        if (repo.findBySdt(dto.getSdt()).isPresent()) {
            throw new LoiThaoTac("Số điện thoại đã được đăng ký!");
        }
        TaiKhoan entity = new TaiKhoan();
        entity.setMaTK(dto.getMaTK() != null ? dto.getMaTK() : "TK" + System.currentTimeMillis());
        entity.setHoTen(dto.getHoTen());
        entity.setSdt(dto.getSdt());
        entity.setEmail(dto.getEmail());
        entity.setMatKhau(dto.getMatKhau());
        entity.setVaiTro("USER");
        entity.setDiemTichLuy(0);
        entity.setHangThanhVien("Đồng");
        return mapToDTO(repo.save(entity));
    }

    @Override
    public TaiKhoanDto updateTaiKhoan(String maTK, TaiKhoanDto dto) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));
        if (dto.getHoTen() != null) entity.setHoTen(dto.getHoTen());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getSdt() != null) entity.setSdt(dto.getSdt());
        if (dto.getMatKhau() != null && !dto.getMatKhau().isEmpty()) {
            entity.setMatKhau(dto.getMatKhau());
        }
        if (dto.getDiemTichLuy() != null) {
            entity.setDiemTichLuy(dto.getDiemTichLuy());
            // Cập nhật hạng thành viên tự động
            int diem = dto.getDiemTichLuy();
            if (diem >= 1000) entity.setHangThanhVien("Vàng");
            else if (diem >= 500) entity.setHangThanhVien("Bạc");
            else entity.setHangThanhVien("Đồng");
        }
        if (dto.getHangThanhVien() != null) entity.setHangThanhVien(dto.getHangThanhVien());
        return mapToDTO(repo.save(entity));
    }

    @Override
    public void deleteTaiKhoan(String maTK) {
        TaiKhoan entity = repo.findById(maTK)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy tài khoản: " + maTK));
        repo.delete(entity);
    }

    @Override
    public boolean login(String sdt, String matKhau) {
        Optional<TaiKhoan> tk = repo.findBySdt(sdt);
        return tk.isPresent() && tk.get().getMatKhau().equals(matKhau);
    }
}
