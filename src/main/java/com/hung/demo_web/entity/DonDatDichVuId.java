package com.hung.demo_web.entity;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.*;

@Embeddable
public class DonDatDichVuId implements Serializable {

    @Column(name = "MaDon")
    private String maDon; 

    @Column(name = "MaDV")
    private String maDV;  

    public String getMaDon(){ 
        return maDon; 
    }
    public void setMaDon(String maDon){ 
        this.maDon = maDon; 
    }

    public String getMaDV(){ 
        return maDV; 
    }
    public void setMaDV(String maDV){ 
        this.maDV = maDV; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DonDatDichVuId that = (DonDatDichVuId) o;
        return Objects.equals(maDon, that.maDon) && Objects.equals(maDV, that.maDV);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maDon, maDV);
    }
}