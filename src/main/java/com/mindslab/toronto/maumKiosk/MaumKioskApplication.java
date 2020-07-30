package com.mindslab.toronto.maumKiosk;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJSONDoc
public class MaumKioskApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MaumKioskApplication.class, args);
    }
    
}
