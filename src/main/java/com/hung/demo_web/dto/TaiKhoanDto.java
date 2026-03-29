package com.hung.demo_web.dto;

import java.sql.Timestamp;

public class TaiKhoanDto {
    private String maTK;
    private String hoTen;   // ← SỬA: "hoten" → "hoTen" cho khớp với getter getHoTen()
    private String sdt;
    private String email;
    private String matKhau;
    private String vaiTro;
    private Integer diemTichLuy;
    private String hangThanhVien;
    private Timestamp ngayTao;

    public void setMaTK(String maTK) { this.maTK = maTK; }
    public String getMaTK() { return maTK; }

    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getHoTen() { return hoTen; }

    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getSdt() { return sdt; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return email; }

    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getMatKhau() { return matKhau; }

    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public String getVaiTro() { return vaiTro; }

    public void setDiemTichLuy(Integer diemTichLuy) { this.diemTichLuy = diemTichLuy; }
    public Integer getDiemTichLuy() { return diemTichLuy; }

    public void setHangThanhVien(String hangThanhVien) { this.hangThanhVien = hangThanhVien; }
    public String getHangThanhVien() { return hangThanhVien; }

    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
    public Timestamp getNgayTao() { return ngayTao; }
}