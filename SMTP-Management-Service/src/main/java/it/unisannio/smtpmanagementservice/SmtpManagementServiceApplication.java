package it.unisannio.smtpmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmtpManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmtpManagementServiceApplication.class, args);
    }

}
