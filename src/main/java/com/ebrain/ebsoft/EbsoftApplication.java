package com.ebrain.ebsoft;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.study.ebsoft"})
@MapperScan("com.study.ebsoft.mapper")
public class EbsoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbsoftApplication.class, args);
    }

}
