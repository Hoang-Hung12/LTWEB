package com.hung.demo_web.mail;

import com.hung.demo_web.dto.DonDatSanDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendMailXacNhanDatSan(DonDatSanDto donDat, String emailTo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailTo);
            helper.setSubject("⚽ Xác nhận đặt sân thành công - Mã đơn: " + donDat.getMaDon());
            String htmlMsg = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                    + "<h2 style='color: #2e6c80;'>Xin chào " + donDat.getTenKhachHang() + ",</h2>"
                    + "<p>Cảm ơn bạn đã đặt sân. Dưới đây là thông tin chi tiết đơn đặt của bạn:</p>"
                    + "<table style='width: 100%; border-collapse: collapse;'>"
                    + "<tr><td style='padding: 8px; border: 1px solid #ddd;'><b>Sân bóng:</b></td><td style='padding: 8px; border: 1px solid #ddd;'>" + donDat.getTenSan() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #ddd;'><b>Ngày đá:</b></td><td style='padding: 8px; border: 1px solid #ddd;'>" + donDat.getNgayDa() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #ddd;'><b>Thời gian:</b></td><td style='padding: 8px; border: 1px solid #ddd;'>" + donDat.getGioBatDau() + " - " + donDat.getGioKetThuc() + "</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #ddd;'><b>Tiền sân:</b></td><td style='padding: 8px; border: 1px solid #ddd;'>" + String.format("%,.0f", donDat.getTienSan()) + " VNĐ</td></tr>"
                    + "<tr><td style='padding: 8px; border: 1px solid #ddd;'><b>Tiền cọc cần thanh toán (30%):</b></td><td style='padding: 8px; border: 1px solid #ddd; color: red; font-weight: bold;'>" + String.format("%,.0f", donDat.getTienCoc()) + " VNĐ</td></tr>"
                    + "</table>"
                    + "<br>"
                    + "<p>Vui lòng chuyển khoản tiền cọc để hệ thống giữ sân cho bạn. Chúc bạn có một trận đấu vui vẻ!</p>"
                    + "</div>";
            helper.setText(htmlMsg, true);
            javaMailSender.send(message);
            System.out.println("Đã gửi mail xác nhận thành công đến: " + emailTo);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi mail: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendMailOtpQuenMatKhau(String hoTen, String otp, String emailTo) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(emailTo);
            helper.setSubject("🔐 Mã OTP đặt lại mật khẩu - ARENAX");
            String htmlMsg = "<div style='font-family: Arial, sans-serif; padding: 24px; border: 1px solid #ddd; border-radius: 8px; max-width: 480px;'>"
                    + "<h2 style='color: #1F4E79;'>Xin chào " + hoTen + ",</h2>"
                    + "<p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn.</p>"
                    + "<p>Mã OTP của bạn là:</p>"
                    + "<div style='font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #2E75B6; "
                    + "background: #DBEAFE; padding: 16px; text-align: center; border-radius: 8px;'>" + otp + "</div>"
                    + "<p style='color: #888; margin-top: 16px;'>Mã có hiệu lực trong <b>5 phút</b>. "
                    + "Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.</p>"
                    + "</div>";
            helper.setText(htmlMsg, true);
            javaMailSender.send(message);
            System.out.println("Đã gửi OTP thành công đến: " + emailTo);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi OTP: " + e.getMessage());
        }
    }
}