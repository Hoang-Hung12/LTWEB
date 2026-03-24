package com.hung.demo_web.dto;

public class HoaDonDto {
    private String maHoaDon;
    private String maDon; 
    private Double tongTienDichVu;
    private Double tongThanhToan;
    private java.sql.Timestamp ngayLap;
    private String maQLLap;
    private String tenQLLap; 
    public String getMaHoaDon() { 
        return maHoaDon; 
    }
    public void setMaHoaDon(String maHoaDon) { 
        this.maHoaDon = maHoaDon; 
    }
    public String getMaDon() { 
        return maDon; 
    }
    public void setMaDon(String maDon) { 
        this.maDon = maDon; 
    }
    public Double getTongTienDichVu() { 
        return tongTienDichVu; 
    }
    public void setTongTienDichVu(Double tongTienDichVu) { 
        this.tongTienDichVu = tongTienDichVu; 
    }
    public Double getTongThanhToan() { 
        return tongThanhToan; 
    }
    public void setTongThanhToan(Double tongThanhToan) { 
        this.tongThanhToan = tongThanhToan; 
    }
    public java.sql.Timestamp getNgayLap() { 
        return ngayLap; 
    }
    public void setNgayLap(java.sql.Timestamp ngayLap) { 
        this.ngayLap = ngayLap; 
    }
    public String getMaQLLap() { 
        return maQLLap; 
    }
    public void setMaQLLap(String maQLLap) { 
        this.maQLLap = maQLLap; 
    }
    public String getTenQLLap() { 
        return tenQLLap; 
    }
    public void setTenQLLap(String tenQLLap) { 
        this.tenQLLap = tenQLLap; 
    }
}
