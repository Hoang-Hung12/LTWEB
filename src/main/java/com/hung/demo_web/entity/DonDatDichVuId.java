package com.hung.demo_web.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
public class DonDatDichVuId implements Serializable{
    @Column(name = "MaDon")
    private String MaDon;

    @Column(name = "MaDV")
    private String MaDV;

    public void setMaDon(String MaDon){
        this.MaDon = MaDon;
    }
    public String getMaDon(){
        return MaDon;
    }

    public void setMaDV(String MaDV){
        this.MaDV = MaDV;
    }
    public String getMaDV(){
        return MaDV;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null||this.getClass() != o.getClass()) return false;
        DonDatDichVuId that = (DonDatDichVuId) o;
        return Objects.equals(that.MaDV, MaDV)&&Objects.equals(that.MaDon, MaDon);    
    }

    @Override
    public int hashCode(){
        return Objects.hash(MaDV, MaDon);
    }
}
