package com.coinliquidity.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LiquidityApp {
    public static void main(String[] args) {
        SpringApplication.run(LiquidityApp.class, args);
    }
}
