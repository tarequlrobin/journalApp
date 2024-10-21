package com.tarequlrobin.journalapp.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendEmail(){
        emailService.sendEmail(
                "tarequlrobin@gmail.com",
                "Testing Mail Service",
                "Hello! This is a test!");
    }
}
