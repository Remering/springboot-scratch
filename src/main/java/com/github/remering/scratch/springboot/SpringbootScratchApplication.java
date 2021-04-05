package com.github.remering.scratch.springboot;

import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootApplication
public class SpringbootScratchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootScratchApplication.class, args);
    }

}
