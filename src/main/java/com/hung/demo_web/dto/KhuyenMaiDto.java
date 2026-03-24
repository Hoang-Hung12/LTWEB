package com.hung.demo_web.dto;

import java.time.LocalDate;

public class KhuyenMaiDto {
    private String maKM;
    private String maCode;
    private String loaiKhuyenMai;
    private Double giaTri;
    private LocalDate ngayHetHan;
    private Integer trangThai;
    public void setMaKM(String maKM){
        this.maKM = maKM;
    }
    public String getMaKM(){
        return maKM;
    }
    public void setMaCode(String maCode){
        this.maCode = maCode;
    }
    public String getMaCode(){
        return maCode;
    }
    public void setLoaiKhuyenMai(String loaiKhuyenMai){
        this.loaiKhuyenMai = loaiKhuyenMai;
    }
    public String getLoaiKhuyenMai(){
        return loaiKhuyenMai;
    }
    public void setGiaTri(Double giaTri){
        this.giaTri = giaTri;
    }
    public Double getGiaTri(){
        return giaTri;
    }
    public void setNgayHetHan(LocalDate ngayHetHan){
        this.ngayHetHan = ngayHetHan;
    }
    public LocalDate getNgayHetHan(){
        return ngayHetHan;
    }
    public void setTrangThai(Integer trangThai){
        this.trangThai = trangThai;
    }
    public Integer getTrangThai(){
        return trangThai;
    }
}
