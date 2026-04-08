package com.hung.demo_web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.DonDatDichVuId;
import com.hung.demo_web.entity.DonDat_DichVu;

@Repository
public interface DonDatDichVuRepository extends JpaRepository<DonDat_DichVu, DonDatDichVuId>{
    List<DonDat_DichVu> findById_maDon(String maDon);
    void deleteById_maDon(String maDon);
}
