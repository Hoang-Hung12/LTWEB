package com.hung.demo_web.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "HoaDon")
public class HoaDonEntity {

    @Id
    @Column(name = "MaHoaDon", length = 30)
    private String maHoaDon;

    @OneToOne
    @JoinColumn(name = "MaDon", nullable = false, unique = true)
    @JsonIgnore  
    private DonDatSan donDatSan;

    @Column(name = "TongTienDichVu")
    private Double tongTienDichVu;

    @Column(name = "TongThanhToan", nullable = false)
    private Double tongThanhToan;

    @Column(name = "NgayLap", insertable = false, updatable = false)
    private Timestamp ngayLap;

    @ManyToOne
    @JoinColumn(name = "MaQLLap", columnDefinition = "VARCHAR(20)")
    @JsonIgnore
    private TaiKhoan nguoiLap;

    public String getMaHoaDon(){ 
        return maHoaDon; 
    }
    public void setMaHoaDon(String maHoaDon){ 
        this.maHoaDon = maHoaDon; 
    }

    public DonDatSan getDonDatSan(){ 
        return donDatSan; 
    }
    public void setDonDatSan(DonDatSan donDatSan){ 
        this.donDatSan = donDatSan; 
    }

    public Double getTongTienDichVu(){ 
        return tongTienDichVu; 
    }
    public void setTongTienDichVu(Double tongTienDichVu){ 
        this.tongTienDichVu = tongTienDichVu; 
    }

    public Double getTongThanhToan(){ 
        return tongThanhToan; 
    }
    public void setTongThanhToan(Double tongThanhToan){ 
        this.tongThanhToan = tongThanhToan; 
    }

    public Timestamp getNgayLap(){ 
        return ngayLap; 
    }
    public void setNgayLap(Timestamp ngayLap){ 
        this.ngayLap = ngayLap; 
    }

    public TaiKhoan getNguoiLap(){ 
        return nguoiLap; 
    }
    public void setNguoiLap(TaiKhoan nguoiLap){ 
        this.nguoiLap = nguoiLap; 
    }
}