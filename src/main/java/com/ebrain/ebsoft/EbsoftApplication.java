package com.ebrain.ebsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.study")
public class EbsoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbsoftApplication.class, args);
    }

}
