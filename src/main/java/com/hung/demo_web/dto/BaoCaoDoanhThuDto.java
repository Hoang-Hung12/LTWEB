package com.hung.demo_web.dto;

import java.util.List;

public class BaoCaoDoanhThuDto {

    // === Tổng quan ===
    private Double tongDoanhThu;          // Tổng tất cả hóa đơn
    private Double tongTienSan;           // Chỉ tiền sân
    private Double tongTienDichVu;        // Chỉ tiền dịch vụ
    private Long   soHoaDon;             // Số hóa đơn đã xuất
    private Long   soDonHoanThanh;       // Số đơn hoàn thành
    private Integer thang;               // Tháng lọc (null = tất cả)
    private Integer nam;                  // Năm lọc

    // === Chi tiết theo tháng (dùng cho API theo-thang) ===
    private List<DoanhThuThangDto> doanhThuTheoThang;

    // === Chi tiết theo sân (dùng cho API theo-san) ===
    private List<DoanhThuSanDto> doanhThuTheoSan;

    // === Top khách hàng (dùng cho API top-khach) ===
    private List<TopKhachHangDto> topKhachHang;

    // -------------------------------------------------------
    // Inner class: doanh thu từng tháng
    // -------------------------------------------------------
    public static class DoanhThuThangDto {
        private Integer thang;
        private Integer nam;
        private Double  doanhThu;
        private Long    soHoaDon;

        public DoanhThuThangDto(Integer thang, Integer nam, Double doanhThu, Long soHoaDon) {
            this.thang    = thang;
            this.nam      = nam;
            this.doanhThu = doanhThu;
            this.soHoaDon = soHoaDon;
        }

        public Integer getThang()    { return thang; }
        public Integer getNam()      { return nam; }
        public Double  getDoanhThu() { return doanhThu; }
        public Long    getSoHoaDon() { return soHoaDon; }
    }

    // -------------------------------------------------------
    // Inner class: doanh thu từng sân
    // -------------------------------------------------------
    public static class DoanhThuSanDto {
        private String maSan;
        private String tenSan;
        private Double doanhThu;
        private Long   soLuotDat;

        public DoanhThuSanDto(String maSan, String tenSan, Double doanhThu, Long soLuotDat) {
            this.maSan     = maSan;
            this.tenSan    = tenSan;
            this.doanhThu  = doanhThu;
            this.soLuotDat = soLuotDat;
        }

        public String getMaSan()      { return maSan; }
        public String getTenSan()     { return tenSan; }
        public Double getDoanhThu()   { return doanhThu; }
        public Long   getSoLuotDat()  { return soLuotDat; }
    }

    // -------------------------------------------------------
    // Inner class: top khách hàng
    // -------------------------------------------------------
    public static class TopKhachHangDto {
        private String maTK;
        private String hoTen;
        private String sdt;
        private Double tongChiTieu;
        private Long   soLanDat;

        public TopKhachHangDto(String maTK, String hoTen, String sdt, Double tongChiTieu, Long soLanDat) {
            this.maTK        = maTK;
            this.hoTen       = hoTen;
            this.sdt         = sdt;
            this.tongChiTieu = tongChiTieu;
            this.soLanDat    = soLanDat;
        }

        public String getMaTK()         { return maTK; }
        public String getHoTen()        { return hoTen; }
        public String getSdt()          { return sdt; }
        public Double getTongChiTieu()  { return tongChiTieu; }
        public Long   getSoLanDat()     { return soLanDat; }
    }

    // -------------------------------------------------------
    // Getters & Setters cho class chính
    // -------------------------------------------------------
    public Double  getTongDoanhThu()    { return tongDoanhThu; }
    public void    setTongDoanhThu(Double v)   { this.tongDoanhThu = v; }

    public Double  getTongTienSan()     { return tongTienSan; }
    public void    setTongTienSan(Double v)    { this.tongTienSan = v; }

    public Double  getTongTienDichVu()  { return tongTienDichVu; }
    public void    setTongTienDichVu(Double v) { this.tongTienDichVu = v; }

    public Long    getSoHoaDon()        { return soHoaDon; }
    public void    setSoHoaDon(Long v)         { this.soHoaDon = v; }

    public Long    getSoDonHoanThanh()  { return soDonHoanThanh; }
    public void    setSoDonHoanThanh(Long v)   { this.soDonHoanThanh = v; }

    public Integer getThang()           { return thang; }
    public void    setThang(Integer v)         { this.thang = v; }

    public Integer getNam()             { return nam; }
    public void    setNam(Integer v)           { this.nam = v; }

    public List<DoanhThuThangDto> getDoanhThuTheoThang() { return doanhThuTheoThang; }
    public void setDoanhThuTheoThang(List<DoanhThuThangDto> v) { this.doanhThuTheoThang = v; }

    public List<DoanhThuSanDto> getDoanhThuTheoSan() { return doanhThuTheoSan; }
    public void setDoanhThuTheoSan(List<DoanhThuSanDto> v) { this.doanhThuTheoSan = v; }

    public List<TopKhachHangDto> getTopKhachHang() { return topKhachHang; }
    public void setTopKhachHang(List<TopKhachHangDto> v) { this.topKhachHang = v; }
}