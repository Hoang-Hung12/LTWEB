package com.hung.demo_web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DichVu")
public class DichVu {
    @Id
    @Column(name = "MaDV")
    private String MaDV;

    @Column(name = "TenDichVu", nullable = false)
    private String tenDichVu;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    // SỬA: Bỏ nullable = false vì DB không khai báo NOT NULL cho cột này
    // Nếu giữ nullable = false → crash khi insert DichVu mà không truyền DonViTinh
    @Column(name = "DonViTinh")
    private String donViTinh;

    public void setMaDV(String MaDV) { this.MaDV = MaDV; }
    public String getMaDV() { return MaDV; }

    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }
    public String getTenDichVu() { return tenDichVu; }

    public void setDonGia(Double donGia) { this.donGia = donGia; }
    public Double getDonGia() { return donGia; }

    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
    public String getDonViTinh() { return donViTinh; }
}