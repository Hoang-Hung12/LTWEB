package com.hung.demo_web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.AnhSan;

@Repository
public interface AnhSanRepository extends JpaRepository<AnhSan, String>{
    List<AnhSan> findByMaSan_MaSan(String maSan);
    
}
