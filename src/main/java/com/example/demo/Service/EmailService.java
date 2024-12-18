package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendVerificationEmail(String recipientEmail, String verificationLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(recipientEmail);
            message.setSubject("Xác minh tài khoản của bạn");
            message.setText("Cảm ơn bạn đã đăng ký tài khoản. Vui lòng nhấp vào liên kết bên dưới để xác minh tài khoản của bạn:\n\n"
                    + verificationLink
                    + "\n\nNếu bạn không yêu cầu xác minh, hãy bỏ qua email này.");
            message.setFrom("no-reply@example.com"); // Thay đổi theo domain của bạn
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể gửi email xác minh.");
        }
    }

    public void sendPasswordResetEmail(String toEmail, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Yêu cầu đặt lại mật khẩu");
        message.setText("Mật khẩu mới của bạn là: " + newPassword +
                "\n\nVui lòng đăng nhập và thay đổi mật khẩu của bạn.");

        mailSender.send(message);
    }
}
