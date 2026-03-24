package com.hung.demo_web.dto;

public class AnhSanDto {
    private String maAnh;
    private String duongDanAnh;
    private String maSan;
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
    
    public void setMaSan(String maSan){
        this.maSan = maSan;
    }
    public String getMaSan(){
        return maSan;
    }
}
