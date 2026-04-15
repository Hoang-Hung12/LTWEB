package com.hung.demo_web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "DonDat_DichVu")
public class DonDat_DichVu {

    @EmbeddedId
    private DonDatDichVuId id;

    @ManyToOne
    @MapsId("maDon") 
    @JoinColumn(name = "MaDon")
    @JsonIgnore
    private DonDatSan donDatSan;

    @ManyToOne
    @MapsId("maDV")  
    @JoinColumn(name = "MaDV")
    @JsonIgnore
    private DichVu dichVu;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "ThanhTien", nullable = false)
    private Double thanhTien;

    public DonDatDichVuId getId() { return id; }
    public void setId(DonDatDichVuId id) { this.id = id; }

    public DonDatSan getDonDatSan() { return donDatSan; }
    public void setDonDatSan(DonDatSan donDatSan) { this.donDatSan = donDatSan; }

    public DichVu getDichVu() { return dichVu; }
    public void setDichVu(DichVu dichVu) { this.dichVu = dichVu; }

    public Integer getSoLuong() { return soLuong; }
    public void setSoLuong(Integer soLuong) { this.soLuong = soLuong; }

    public Double getThanhTien() { return thanhTien; }
    public void setThanhTien(Double thanhTien) { this.thanhTien = thanhTien; }
}