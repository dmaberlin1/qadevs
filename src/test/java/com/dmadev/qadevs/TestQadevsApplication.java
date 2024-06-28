package com.dmadev.qadevs;

import org.springframework.boot.SpringApplication;

public class TestQadevsApplication {

    public static void main(String[] args) {
        SpringApplication.from(QadevsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
