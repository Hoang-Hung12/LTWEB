package com.hung.demo_web.mail;

import com.hung.demo_web.dto.DonDatSanDto;

public interface MailService {
    void sendMailXacNhanDatSan(DonDatSanDto donDat, String emailTo);
}