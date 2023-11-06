package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching //spring自带的缓存机制
public class SpringbootReverseEngineeringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootReverseEngineeringApplication.class, args);
    }

}
