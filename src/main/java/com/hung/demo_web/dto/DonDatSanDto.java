package com.hung.demo_web.dto;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class DonDatSanDto {
    private String maDon;
    private Timestamp ngayDat;
    private LocalDate ngayDa;
    private LocalTime gioBatDau;
    private LocalTime gioKetThuc;
    private Double tienSan;
    private Double tienCoc;
    private Integer diemSuDung;
    private Integer diemThuong;
    private String trangThai;
    private String maKH;
    private String tenKhachHang;
    private String sdtKhachHang;
    private String maSan;
    private String tenSan;
    private String maCodeKhuyenMai;
    private String phuongThucThanhToan;
    private Boolean daThanhToanCoc;
    private String chungTuThanhToan;
    private String maAdminXacNhanThanhToan;
    private Timestamp thoiGianXacNhanThanhToan;
    public String getMaDon(){
        return maDon; 
    }
    public void setMaDon(String maDon){ 
        this.maDon = maDon; 
    }
    public Timestamp getNgayDat(){ 
        return ngayDat; 
    }
    public void setNgayDat(Timestamp ngayDat){ 
        this.ngayDat = ngayDat; 
    }
    public LocalDate getNgayDa(){ 
        return ngayDa; 
    }
    public void setNgayDa(LocalDate ngayDa){ 
        this.ngayDa = ngayDa; 
    }
    public LocalTime getGioBatDau(){ 
        return gioBatDau; 
    }
    public void setGioBatDau(LocalTime gioBatDau){ 
        this.gioBatDau = gioBatDau; 
    }
    public LocalTime getGioKetThuc(){ 
        return gioKetThuc; 
    }
    public void setGioKetThuc(LocalTime gioKetThuc){ 
        this.gioKetThuc = gioKetThuc; 
    }
    public Double getTienSan(){ 
        return tienSan; 
    }
    public void setTienSan(Double tienSan){ 
        this.tienSan = tienSan; 
    }
    public Double getTienCoc(){ 
        return tienCoc; 
    }
    public void setTienCoc(Double tienCoc){ 
        this.tienCoc = tienCoc; 
    }
    public Integer getDiemSuDung(){ 
        return diemSuDung; 
    }
    public void setDiemSuDung(Integer diemSuDung){ 
        this.diemSuDung = diemSuDung; 
    }
    public Integer getDiemThuong(){ 
        return diemThuong; 
    }
    public void setDiemThuong(Integer diemThuong){ 
        this.diemThuong = diemThuong; 
    }
    public String getTrangThai(){ 
        return trangThai; 
    }
    public void setTrangThai(String trangThai){ 
        this.trangThai = trangThai; 
    }
    public String getMaKH(){ 
        return maKH; 
    }
    public void setMaKH(String maKH){ 
        this.maKH = maKH; 
    }
    public String getTenKhachHang(){ 
        return tenKhachHang; 
    }
    public void setTenKhachHang(String tenKhachHang){ 
        this.tenKhachHang = tenKhachHang; 
    }
    public String getSdtKhachHang(){ 
        return sdtKhachHang; 
    }
    public void setSdtKhachHang(String sdtKhachHang){ 
        this.sdtKhachHang = sdtKhachHang; 
    }
    public String getMaSan(){ 
        return maSan; 
    }
    public void setMaSan(String maSan){ 
        this.maSan = maSan; 
    }
    public String getTenSan(){ 
        return tenSan; 
    }
    public void setTenSan(String tenSan){ 
        this.tenSan = tenSan; 
    }
    public String getMaCodeKhuyenMai(){ 
        return maCodeKhuyenMai; 
    }
    public void setMaCodeKhuyenMai(String maCodeKhuyenMai){ 
        this.maCodeKhuyenMai = maCodeKhuyenMai; 
    }
    public String getPhuongThucThanhToan(){
        return phuongThucThanhToan;
    }
    public void setPhuongThucThanhToan(String phuongThucThanhToan){
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
    public Boolean getDaThanhToanCoc(){
        return daThanhToanCoc;
    }
    public void setDaThanhToanCoc(Boolean daThanhToanCoc){
        this.daThanhToanCoc = daThanhToanCoc;
    }
    public String getChungTuThanhToan() {
        return chungTuThanhToan;
    }
    public void setChungTuThanhToan(String chungTuThanhToan) {
        this.chungTuThanhToan = chungTuThanhToan;
    }
    public String getMaAdminXacNhanThanhToan() {
        return maAdminXacNhanThanhToan;
    }
    public void setMaAdminXacNhanThanhToan(String maAdminXacNhanThanhToan) {
        this.maAdminXacNhanThanhToan = maAdminXacNhanThanhToan;
    }
    public Timestamp getThoiGianXacNhanThanhToan() {
        return thoiGianXacNhanThanhToan;
    }
    public void setThoiGianXacNhanThanhToan(Timestamp thoiGianXacNhanThanhToan) {
        this.thoiGianXacNhanThanhToan = thoiGianXacNhanThanhToan;
    }
}