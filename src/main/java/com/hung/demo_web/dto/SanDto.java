package com.hung.demo_web.dto;

public class SanDto {
    private String maSan;
    private String tenSan;
    private String diaChi;
    private String moTa;
    private String tienIch;
    private String anhChinh;
    private Double giaThue;
    private String trangThai;
    private String tenLoaiSan;
    private Integer soNguoi;
    
    public void setMaSan(String maSan){
        this.maSan = maSan;
    }
    public String getMaSan(){
        return maSan;
    }

    public void setTenSan(String tenSan){
        this.tenSan = tenSan;
    }
    public String getTenSan(){
        return tenSan;
    }

    public void setDiaChi(String diaChi){
        this.diaChi = diaChi;
    }
    public String getDiachi(){
        return diaChi;
    }

    public void setMoTa(String moTa){
        this.moTa = moTa;
    }
    public String getMoTa(){
        return moTa;
    }

    public void setTienIch(String tienIch){
        this.tienIch = tienIch;
    }
    public String getTienIch(){
        return tienIch;
    }

    public void setAnhChinh(String anhChinh){
        this.anhChinh = anhChinh;
    }
    public String getAnhChinh(){
        return anhChinh;
    }

    public void setGiaThue(Double giaThue){
        this.giaThue = giaThue;
    }
    public Double getGiaThue(){
        return giaThue;
    }

    public void setTrangThai(String trangThai){
        this.trangThai = trangThai;
    }
    public String getTrangThai(){
        return trangThai;
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
