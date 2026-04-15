package com.hung.demo_web.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name = "MaTK", length = 20)
    private String maTK;
    @Column(name = "MatKhau", nullable = false)
    private String matKhau;
    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;
    @Column(name = "Email", nullable = false, unique = true, length = 150)
    private String email;
    @Column(name = "SDT", nullable = false, unique = true, length = 15)
    private String sdt;
    @Column(name = "VaiTro", length = 20)
    private String vaiTro;
    @Column(name = "DiemTichLuy")
    private Integer diemTichLuy;
    @Column(name = "HangThanhVien", length = 20)
    private String hangThanhVien;
    @Column(name = "NgayTao", insertable = false, updatable = false)
    private Timestamp ngayTao;
    public String getMaTK(){ 
        return maTK; 
    }
    public void setMaTK(String maTK){ 
        this.maTK = maTK; 
    }
    public String getMatKhau(){ 
        return matKhau; 
    }
    public void setMatKhau(String matKhau){ 
        this.matKhau = matKhau; 
    }
    public String getHoTen(){ 
        return hoTen; 
    }
    public void setHoTen(String hoTen){ 
        this.hoTen = hoTen;
    }
    public String getEmail(){ 
        return email; 
    }
    public void setEmail(String email){ 
        this.email = email; 
    }
    public String getSdt(){ 
        return sdt; 
    }
    public void setSdt(String sdt){ 
        this.sdt = sdt; 
    }
    public String getVaiTro(){ 
        return vaiTro; 
    }
    public void setVaiTro(String vaiTro){ 
        this.vaiTro = vaiTro; 
    }
    public Integer getDiemTichLuy(){ 
        return diemTichLuy; 
    }
    public void setDiemTichLuy(Integer diemTichLuy){ 
        this.diemTichLuy = diemTichLuy; 
    }
    public String getHangThanhVien(){ 
        return hangThanhVien; 
    }
    public void setHangThanhVien(String hangThanhVien){ 
        this.hangThanhVien = hangThanhVien; 
    }
    public Timestamp getNgayTao(){ 
        return ngayTao; 
    }
    public void setNgayTao(Timestamp ngayTao){ 
        this.ngayTao = ngayTao; 
    }
}