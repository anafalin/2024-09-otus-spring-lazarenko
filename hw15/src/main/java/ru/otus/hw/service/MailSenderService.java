package ru.otus.hw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.EmailNotification;

@Slf4j
@Service
public class MailSenderService {

    public void sendMail(EmailNotification emailNotification) {
        log.info("Sent to {}:{}", emailNotification.getEmailTo(), emailNotification.getSubject());
    }
}