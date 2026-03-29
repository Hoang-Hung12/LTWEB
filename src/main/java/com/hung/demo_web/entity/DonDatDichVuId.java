package com.hung.demo_web.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Embeddable
public class DonDatDichVuId implements Serializable{
    @Column(name = "MaDon")
    private String maDon;

    @Column(name = "MaDV")
    private String maDV;

    public void setMaDon(String maDon){
        this.maDon = maDon;
    }
    public String getMaDon(){
        return maDon;
    }

    public void setMaDV(String maDV){
        this.maDV = maDV;
    }
    public String getMaDV(){
        return maDV;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null||this.getClass() != o.getClass()) return false;
        DonDatDichVuId that = (DonDatDichVuId) o;
        return Objects.equals(that.maDV, maDV)&&Objects.equals(that.maDon, maDon);    
    }

    @Override
    public int hashCode(){
        return Objects.hash(maDV, maDon);
    }
}
