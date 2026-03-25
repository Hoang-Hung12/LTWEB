package com.hung.demo_web.service;

import java.util.List;

import com.hung.demo_web.dto.TaiKhoanDto;

public interface TaiKhoanService {
    List<TaiKhoanDto> getAllTaiKhoan();
    TaiKhoanDto getBySdt(String sdt);
    TaiKhoanDto createTaiKhoan(TaiKhoanDto dto);
    boolean login(String sdt, String matKhau);
}
