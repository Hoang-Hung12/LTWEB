package com.hung.demo_web.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {
    @Id
    @Column(name = "MaKM")
    private String MaKM;

    @Column(name = "MaCode", nullable = false, unique = true)
    private String maCode;

    @Column(name = "LoaiKhuyenMai", nullable = false)
    private String loaiKhuyenMai;

    @Column(name = "GiaTri", nullable = false)
    private Double giaTri;

    @Column(name = "NgayHetHan", nullable = false)
    private LocalDate ngayHetHan;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public void setMaKM(String MaKM){
        this.MaKM = MaKM;
    }
    public String getMaKM(){
        return MaKM;
    }
    public void setMaCode(String maCode){
        this.maCode = maCode;
    }
    public String getMaCode(){
        return this.maCode;
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
