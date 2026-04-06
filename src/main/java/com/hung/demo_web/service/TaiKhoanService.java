package com.hung.demo_web.service;

import java.util.List;
import com.hung.demo_web.dto.TaiKhoanDto;

public interface TaiKhoanService {
    List<TaiKhoanDto> getAllTaiKhoan();
    TaiKhoanDto getBySdt(String sdt);
    TaiKhoanDto getById(String maTK);
    TaiKhoanDto createTaiKhoan(TaiKhoanDto dto);
    TaiKhoanDto updateTaiKhoan(String maTK, TaiKhoanDto dto);
    void deleteTaiKhoan(String maTK);
    boolean login(String sdt, String matKhau);
}
