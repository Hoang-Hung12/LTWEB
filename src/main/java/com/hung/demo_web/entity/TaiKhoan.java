package com.hung.demo_web.entity;

import java.security.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name = "MaTK")
    private String MaTK;

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

    public String getMaTK(){
        return MaTK;
    }
    public void setMaTK(String MaTK){
        this.MaTK = MaTK;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setHoTen(String hoTen){
        this.hoTen = hoTen;
    }
    public String getHoTen(){
        return hoTen;
    }
    public void setsdt(String sdt){
        this.sdt = sdt;
    }
    public String getsdt(){
        return sdt;
    }
    public void setVaiTro(String vaiTro){
        this.vaiTro = vaiTro;
    }
    public String getVaiTro(){
        return vaiTro;
    }
    public void setDiemTichLuy(Integer diemTichLuy){
        this.diemTichLuy = diemTichLuy;
    }
    public Integer getDiemTichLuy(){
        return diemTichLuy;
    }
    public void setMatKhau(String maKhau){
        this.matKhau = maKhau;
    }
    public String getMatKhau(){
        return matKhau;
    }
    public void setHangThanhVien(String hangThanhVien){
        this.hangThanhVien = hangThanhVien;
    }
    public String getHangThanhVien(){
        return hangThanhVien;
    }
    public void setNgayTao(Timestamp ngayTao){
        this.ngayTao = ngayTao;
    }
    public Timestamp getNgayTao(){
        return ngayTao;
    }
}
