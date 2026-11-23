
package com.qianyi.sportstrainingassistant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String testEmail;

    @Test
    void shouldSendTestEmail() {

        emailService.sendEmail(
                testEmail,
                "Sports Training Assistant - Test",
                "This is our first test email!"
        );
    }
    @Value("${spring.mail.password}")
    private String mailPassword;
    @Test
    void checkMailConfiguration() {

        System.out.println("Password length: " + mailPassword.length());

        System.out.println("Matches Windows variable: " +
                mailPassword.equals(System.getenv("MAIL_PASSWORD")));

        System.out.println("Has spaces: " +
                mailPassword.chars().anyMatch(Character::isWhitespace));
    }
}
