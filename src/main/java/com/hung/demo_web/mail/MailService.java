package com.hung.demo_web.mail;

import com.hung.demo_web.dto.DonDatSanDto;

public interface MailService {
    // Gửi mail xác nhận đặt sân
    void sendMailXacNhanDatSan(DonDatSanDto donDat, String emailTo);

    // Gửi OTP quên mật khẩu
    void sendMailOtpQuenMatKhau(String hoTen, String otp, String emailTo);
}