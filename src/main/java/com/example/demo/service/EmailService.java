package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendReminder(String to,
                             String medicine,
                             String dosage,
                             String time) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);

        message.setSubject("Medicine Reminder");

        message.setText(
                "Hello,\n\n" +
                        "It's time to take your medicine.\n\n" +
                        "Medicine : " + medicine +
                        "\nDosage : " + dosage +
                        "\nTime : " + time +
                        "\n\nStay Healthy!"
        );

        mailSender.send(message);

    }

}