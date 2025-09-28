package com.ippclub.ippdicebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ippclub.ippdicebackend.mapper")
public class IppDiceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(IppDiceBackendApplication.class, args);
    }

}