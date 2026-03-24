    package com.hung.demo_web.entity;

    import jakarta.persistence.*;

    @Entity
    @Table(name = "DonDat_DichVu")
    public class DonDat_DichVu {
        @EmbeddedId
        private DonDatDichVuId id;
        
        @ManyToOne
        @MapsId("MaDon")
        @JoinColumn(name = "MaDon")
        private DonDatSan donDatSan;

        @ManyToOne
        @MapsId("MaDV")
        @JoinColumn(name = "MaDV")
        private DichVu dichVu;

        @Column(name = "SoLuong")
        private Integer soLuong;

        @Column(name = "ThanhTien", nullable = false)
        private Double thanhTien;

        public void setId(DonDatDichVuId id){
            this.id = id;
        }
        public DonDatDichVuId getId(){
            return id;
        }

        public void setDonDatSan(DonDatSan donDatSan){
            this.donDatSan = donDatSan;
        }
        public DonDatSan getDonDatSan(){
            return donDatSan;
        }

        public void setDichVu(DichVu dichVu){
            this.dichVu = dichVu;
        }
        public DichVu getDichVu(){
            return dichVu;
        }
        
        public void setSoLuong(Integer soLuong){
            this.soLuong = soLuong;
        }
        public Integer getSoLuong(){
            return soLuong;
        }

        public void setThanhTien(Double thanhTien){
            this.thanhTien = thanhTien;
        }
        public Double getThanhTien(){
            return thanhTien;
        }
    }
