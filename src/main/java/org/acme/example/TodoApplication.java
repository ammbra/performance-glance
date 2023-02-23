package org.acme.example;

import jdk.jfr.consumer.EventStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {

        SpringApplication.run(TodoApplication.class, args);
    }
}
