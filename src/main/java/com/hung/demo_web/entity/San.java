package com.hung.demo_web.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "San")
public class San {
    @Id
    @Column(name = "MaSan")
    private String maSan;

    @Column(name = "TenSan", nullable = false)
    private String tenSan;
    
    @Column(name = "DiaChi", nullable = false)
    private String diaChi;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "TienIch")
    private String tienIch;

    @Column(name = "AnhChinh")
    private String anhChinh;

    @Column(name = "GiaThue", nullable = false)
    private Double giaThue;

    @Column(name = "TrangThai")
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "MaLoaiSan")
    private LoaiSan loaiSan;

    public void setMaSan(String maSan){
        this.maSan = maSan;
    }
    public String getMaSan(){
        return maSan;
    }

    public void seTenSan(String tenSan){
        this.tenSan = tenSan;
    }
    public String getTenSan(){
        return tenSan;
    }

    public void setDiaChi(String diaChi){
        this.diaChi = diaChi;
    }
    public String getDiaChi(){
        return diaChi;
    }

    public void setMota(String moTa){
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

    public void setLoaiSan(LoaiSan loaiSan){
        this.loaiSan = loaiSan;
    }
    public LoaiSan getLoaiSan(){
        return loaiSan;
    }
}
