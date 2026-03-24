package com.hung.demo_web.dto;

public class DonDatDichVuDto {
    private String maDon;
    private String maDV;
    private String tenDichVu; 
    private Double donGia;    
    private Integer soLuong;
    private Double thanhTien;
    public String getMaDon(){ 
        return maDon; 
    }
    public void setMaDon(String maDon){ 
        this.maDon = maDon; 
    }
    public String getMaDV(){ 
        return maDV; 
    }
    public void setMaDV(String maDV){ 
        this.maDV = maDV; 
    }
    public String getTenDichVu(){ 
        return tenDichVu; 
    }
    public void setTenDichVu(String tenDichVu){ 
        this.tenDichVu = tenDichVu; 
    }
    public Double getDonGia(){ 
        return donGia; 
    }
    public void setDonGia(Double donGia){ 
        this.donGia = donGia; 
    }
    public Integer getSoLuong(){ 
        return soLuong; 
    }
    public void setSoLuong(Integer soLuong){ 
        this.soLuong = soLuong; 
    }
    public Double getThanhTien(){ 
        return thanhTien; 
    }
    public void setThanhTien(Double thanhTien){ 
        this.thanhTien = thanhTien; 
    }
}
