package com.hung.demo_web.service;

import com.hung.demo_web.dto.BaoCaoDoanhThuDto;

public interface BaoCaoDoanhThuService {

    /**
     * Tổng quan doanh thu: có thể lọc theo tháng và năm.
     * Nếu thang = null → lấy cả năm.
     * Nếu cả hai = null → lấy toàn bộ.
     */
    BaoCaoDoanhThuDto tongQuan(Integer thang, Integer nam);

    /**
     * Doanh thu theo từng tháng trong một năm cụ thể.
     */
    BaoCaoDoanhThuDto theoThang(Integer nam);

    /**
     * Doanh thu theo từng sân, lọc theo tháng/năm.
     */
    BaoCaoDoanhThuDto theoSan(Integer thang, Integer nam);

    /**
     * Top N khách hàng chi tiêu nhiều nhất, lọc theo tháng/năm.
     */
    BaoCaoDoanhThuDto topKhachHang(Integer thang, Integer nam, Integer topN);
}