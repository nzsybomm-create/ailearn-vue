package com.ailearn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ailearn.mapper")
public class AilearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(AilearnApplication.class, args);
    }
}
