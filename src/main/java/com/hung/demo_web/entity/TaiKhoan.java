package com.hung.demo_web.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name = "MaTK")
    private String maTK; // SỬA: MaTK → maTK (field phải bắt đầu chữ thường)

    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "SDT", nullable = false, unique = true)
    private String sdt;

    @Column(name = "VaiTro")
    private String vaiTro;

    @Column(name = "DiemTichLuy")
    private Integer diemTichLuy;

    @Column(name = "HangThanhVien")
    private String hangThanhVien;

    @Column(name = "NgayTao", insertable = false, updatable = false)
    private Timestamp ngayTao;

    public String getMaTK() { return maTK; }
    public void setMaTK(String maTK) { this.maTK = maTK; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public Integer getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(Integer diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    public String getHangThanhVien() { return hangThanhVien; }
    public void setHangThanhVien(String hangThanhVien) { this.hangThanhVien = hangThanhVien; }

    public Timestamp getNgayTao() { return ngayTao; }
    public void setNgayTao(Timestamp ngayTao) { this.ngayTao = ngayTao; }
}