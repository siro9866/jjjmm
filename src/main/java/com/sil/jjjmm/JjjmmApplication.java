package com.sil.jjjmm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JjjmmApplication {

    public static void main(String[] args) {
        SpringApplication.run(JjjmmApplication.class, args);
    }

}
