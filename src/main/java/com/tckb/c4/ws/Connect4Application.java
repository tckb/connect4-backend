package com.tckb.c4.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The holy-grail of the app, this is boot-class for the web-service
 * <p>
 * @author tckb
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.tckb.c4")
public class Connect4Application {

    public static void main(String[] args) {
        SpringApplication.run(Connect4Application.class, args);
    }

}
