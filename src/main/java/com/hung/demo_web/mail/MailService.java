package com.hung.demo_web.mail;

import com.hung.demo_web.dto.DonDatSanDto;

public interface MailService {
    // Hàm này sẽ nhận thông tin Đơn đặt sân và email của khách để bắn thư đi
    void sendMailXacNhanDatSan(DonDatSanDto donDat, String emailTo);
}