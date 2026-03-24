package com.hung.demo_web.dto;

public class DichVuDto {
    private String maDV;
    private String tenDichVu;
    private Double donGia;
    private String donViTinh;
    public void setMaDV(String maDV){
        this.maDV = maDV;
    }
    public String getMaDV(){
        return maDV;
    }
    public void setTenDichVu(String tenDichVu){
        this.tenDichVu = tenDichVu;
    }
    public String getTenDichVu(){
        return tenDichVu;
    }
    public void setDonGia(Double donGia){
        this.donGia = donGia;
    }
    public Double getDonGia(){
        return donGia;
    }
    public void setDonViTinh(String donViTinh){
        this.donViTinh = donViTinh;
    }
    public String getDonViTinh(){
        return donViTinh;
    }
}
