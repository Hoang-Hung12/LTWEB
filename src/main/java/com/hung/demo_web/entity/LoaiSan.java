package com.hung.demo_web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LoaiSan")
public class LoaiSan {
    @Id
    @Column(name = "MaLoaiSan")
    private String maLoaiSan;

    @Column(name = "TenLoaiSan", nullable = false)
    private String tenLoaiSan;

    @Column(name = "SoNguoi", nullable = false)
    private Integer soNguoi;

    public void setMaLoaiSan(String maLoaiSan){
        this.maLoaiSan = maLoaiSan;
    }
    public String getMaLoaiSan(){
        return maLoaiSan;
    }
    public void setTenLoaiSan(String tenLoaiSan){
        this.tenLoaiSan = tenLoaiSan;
    }
    public String getTenLoaiSan(){
        return this.tenLoaiSan;
    }
    public void setSoNguoi(Integer soNguoi){
        this.soNguoi = soNguoi;
    }
    public Integer getSoNguoi(){
        return soNguoi;
    }
}
