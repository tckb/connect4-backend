package com.tckb.c4.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Connect4Application {

    public static void main(String[] args) {
        SpringApplication.run(Connect4Application.class, args);
    }
}
