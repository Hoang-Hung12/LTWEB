package com.hung.demo_web.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "San")
public class San {
    @Id
    @Column(name = "MaSan", length = 20)
    private String maSan;

    @Column(name = "TenSan", nullable = false, length = 150)
    private String tenSan;

    @Column(name = "DiaChi", nullable = false, length = 255)
    private String diaChi;

    @Column(name = "MoTa", length = 500)
    private String moTa;

    @Column(name = "TienIch", length = 500)
    private String tienIch;

    @Column(name = "AnhChinh", length = 500)
    private String anhChinh;

    @Column(name = "GiaThue", nullable = false)
    private Double giaThue;

    @Column(name = "TrangThai", length = 20)
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "MaLoaiSan", columnDefinition = "VARCHAR(20)")
    @JsonIgnore
    private LoaiSan loaiSan;

    public void setMaSan(String maSan) { this.maSan = maSan; }
    public String getMaSan() { return maSan; }

    // SỬA: seTenSan → setTenSan (thiếu chữ 't', sai JavaBean → Swagger lỗi + NPE khi gọi)
    public void setTenSan(String tenSan) { this.tenSan = tenSan; }
    public String getTenSan() { return tenSan; }

    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public String getDiaChi() { return diaChi; }

    // SỬA: setMota → setMoTa (sai chữ hoa, không khớp field moTa)
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getMoTa() { return moTa; }

    public void setTienIch(String tienIch) { this.tienIch = tienIch; }
    public String getTienIch() { return tienIch; }

    public void setAnhChinh(String anhChinh) { this.anhChinh = anhChinh; }
    public String getAnhChinh() { return anhChinh; }

    public void setGiaThue(Double giaThue) { this.giaThue = giaThue; }
    public Double getGiaThue() { return giaThue; }

    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getTrangThai() { return trangThai; }

    public void setLoaiSan(LoaiSan loaiSan) { this.loaiSan = loaiSan; }
    public LoaiSan getLoaiSan() { return loaiSan; }
}