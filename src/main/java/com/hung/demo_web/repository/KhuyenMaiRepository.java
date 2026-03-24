package com.hung.demo_web.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.KhuyenMai;

@Repository
public interface KhuyenMaiRepository  extends JpaRepository<KhuyenMai, String>{
    Optional<KhuyenMai> findByMaCode(String maCode);
    
}
