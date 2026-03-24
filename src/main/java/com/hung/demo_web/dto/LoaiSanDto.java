package com.hung.demo_web.dto;

public class LoaiSanDto {
    private String maLoaiSan;
    private String tenLoaiSan;
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
        return tenLoaiSan;
    }
    
    public void setSoNguoi(Integer soNguoi){
        this.soNguoi = soNguoi;
    }
    public Integer getSoNguoi(){
        return soNguoi;
    }
}
