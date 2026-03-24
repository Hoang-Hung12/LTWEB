package com.hung.demo_web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hung.demo_web.entity.DichVu;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, String>{
    
}
