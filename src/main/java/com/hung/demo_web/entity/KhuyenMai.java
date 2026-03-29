package com.hung.demo_web.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "KhuyenMai")
public class KhuyenMai {
    @Id
    @Column(name = "MaKM")
    private String maKM; // SỬA: MaKM → maKM

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

    public String getMaKM() { return maKM; }
    public void setMaKM(String maKM) { this.maKM = maKM; }

    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }

    public String getLoaiKhuyenMai() { return loaiKhuyenMai; }
    public void setLoaiKhuyenMai(String loaiKhuyenMai) { this.loaiKhuyenMai = loaiKhuyenMai; }

    public Double getGiaTri() { return giaTri; }
    public void setGiaTri(Double giaTri) { this.giaTri = giaTri; }

    public LocalDate getNgayHetHan() { return ngayHetHan; }
    public void setNgayHetHan(LocalDate ngayHetHan) { this.ngayHetHan = ngayHetHan; }

    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
}