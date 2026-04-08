package com.hung.demo_web.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.DonDatSan;

@Repository
public interface DonDatSanRepository extends JpaRepository<DonDatSan, String>{
    List<DonDatSan> findByKhachHang_maTK(String maTK);
    List<DonDatSan> findBySan_MaSan(String maSan);
    List<DonDatSan> findBySan_MaSanAndNgayDa(String maSan, LocalDate ngayDa);
}