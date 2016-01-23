package com.tckb.c4.ws;

import com.tckb.c4.model.exception.MaxPlayerRegisteredException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Connect4Application {

    public static void main(String[] args) throws MaxPlayerRegisteredException {
        SpringApplication.run(Connect4Application.class, args);
    }
}
