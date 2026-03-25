package com.hung.demo_web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "AnhSan")
public class AnhSan {
    @Id
    @Column(name = "MaAnh")
    private String maAnh;

    @Column(name = "DuongDanAnh", nullable = false)
    private String  duongDanAnh;

    @ManyToOne
    @JoinColumn(name = "MaSan")
    @JsonIgnore
    private San maSan;

    public void setMaAnh(String maAnh){
        this.maAnh = maAnh;
    }
    public String getMaAnh(){
        return maAnh;
    }

    public void setDuongDanAnh(String duongDanAnh){
        this.duongDanAnh = duongDanAnh;
    }
    public String getDuongDanAnh(){
        return duongDanAnh;
    }

    public void setMaSan(San maSan){
        this.maSan = maSan;
    }
    public San getMaSan(){
        return maSan;
    }
}
