package com.hung.demo_web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DichVu")
public class DichVu {
    @Id
    @Column(name = "MaDV")
    private String maDV; 

    @Column(name = "TenDichVu", nullable = false)
    private String tenDichVu;

    @Column(name = "DonGia", nullable = false)
    private Double donGia;

    @Column(name = "DonViTinh") 
    private String donViTinh;

    public String getMaDV() { return maDV; }
    public void setMaDV(String maDV) { this.maDV = maDV; }

    public String getTenDichVu() { return tenDichVu; }
    public void setTenDichVu(String tenDichVu) { this.tenDichVu = tenDichVu; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }

    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }
}