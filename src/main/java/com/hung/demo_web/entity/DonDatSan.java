package com.hung.demo_web.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "DonDatSan")
public class DonDatSan {
    @Id
    @Column(name = "MaDon")
    private String maDon; // SỬA: MaDon → maDon

    @Column(name = "NgayDat", insertable = false, updatable = false)
    private Timestamp ngayDat;

    @Column(name = "NgayDa", nullable = false)
    private LocalDate ngayDa;

    @Column(name = "GioBatDau", nullable = false)
    private LocalTime gioBatDau;

    @Column(name = "GioKetThuc", nullable = false)
    private LocalTime gioKetThuc;

    @Column(name = "TienSan", nullable = false)
    private Double tienSan;

    @Column(name = "TienCoc")
    private Double tienCoc;

    @Column(name = "DiemSuDung")
    private Integer diemSuDung;

    @Column(name = "DiemThuong")
    private Integer diemThuong;

    @Column(name = "TrangThai")
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "MaKM")
    @JsonIgnore
    private KhuyenMai khuyenMai;

    @ManyToOne
    @JoinColumn(name = "MaSan", nullable = false)
    @JsonIgnore
    private San san;

    @ManyToOne
    @JoinColumn(name = "MaKH", nullable = false)
    @JsonIgnore
    private TaiKhoan khachHang;

    public String getMaDon() { return maDon; }
    public void setMaDon(String maDon) { this.maDon = maDon; }

    public Timestamp getNgayDat() { return ngayDat; }
    public void setNgayDat(Timestamp ngayDat) { this.ngayDat = ngayDat; }

    public LocalDate getNgayDa() { return ngayDa; }
    public void setNgayDa(LocalDate ngayDa) { this.ngayDa = ngayDa; }

    public LocalTime getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(LocalTime gioBatDau) { this.gioBatDau = gioBatDau; }

    public LocalTime getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(LocalTime gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public Double getTienSan() { return tienSan; }
    public void setTienSan(Double tienSan) { this.tienSan = tienSan; }

    public Double getTienCoc() { return tienCoc; }
    public void setTienCoc(Double tienCoc) { this.tienCoc = tienCoc; }

    public Integer getDiemSuDung() { return diemSuDung; }
    public void setDiemSuDung(Integer diemSuDung) { this.diemSuDung = diemSuDung; }

    public Integer getDiemThuong() { return diemThuong; }
    public void setDiemThuong(Integer diemThuong) { this.diemThuong = diemThuong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public KhuyenMai getKhuyenMai() { return khuyenMai; }
    public void setKhuyenMai(KhuyenMai khuyenMai) { this.khuyenMai = khuyenMai; }

    public San getSan() { return san; }
    public void setSan(San san) { this.san = san; }

    public TaiKhoan getKhachHang() { return khachHang; }
    public void setKhachHang(TaiKhoan khachHang) { this.khachHang = khachHang; }
}