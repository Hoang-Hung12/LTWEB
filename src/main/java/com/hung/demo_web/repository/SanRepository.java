package com.hung.demo_web.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.San;

@Repository
public interface SanRepository extends JpaRepository<San, String>{
    List<San> findByTrangThai(String trangThai);
    
}
