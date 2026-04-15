package com.hung.demo_web.job;

import com.hung.demo_web.entity.KhuyenMai;
import com.hung.demo_web.repository.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component // Đánh dấu để Spring Boot biết đây là một nhân viên chạy ngầm
public class KhuyenMaiJob {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    // =========================================================================
    // CRON FORMAT: Giây - Phút - Giờ - Ngày trong tháng - Tháng - Ngày trong tuần
    // "0 0 0 * * ?" nghĩa là: 0 giây, 0 phút, 0 giờ, mỗi ngày, mỗi tháng, bất kỳ thứ mấy
    // =========================================================================
    @Scheduled(cron = "0 0 0 * * ?")
    public void quetMaKhuyenMaiHetHan() {
        System.out.println("⏳ [JOB NỬA ĐÊM] Bắt đầu quét các mã khuyến mãi hết hạn...");
        
        // Fix lỗi 9: Query thẳng DB để lọc mã, không dùng findAll()
        List<KhuyenMai> hetHan = khuyenMaiRepository.findByTrangThaiAndNgayHetHanBefore(1, java.time.LocalDate.now());
        
        for (KhuyenMai km : hetHan) {
            km.setTrangThai(0);
            System.out.println("   -> Đã khóa tự động mã: " + km.getMaCode());
        }
        
        if (!hetHan.isEmpty()) {
            khuyenMaiRepository.saveAll(hetHan); // Dùng saveAll hiệu suất cao hơn
        }
        System.out.println("✅ [JOB NỬA ĐÊM] Quét xong! Đã khóa thành công " + hetHan.size() + " mã.");
    }
}