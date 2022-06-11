package com.ldl.petsystemspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

@MapperScan("com.ldl.petsystemspringboot.mapper")
@SpringBootApplication
public class PetsystemspringbootApplication {
    Deque<Integer> deque = new ArrayDeque<>();

    public static void main(String[] args) {
        SpringApplication.run(PetsystemspringbootApplication.class, args);
    }

}
