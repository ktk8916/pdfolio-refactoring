package com.playdata.pdfolio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class PdfolioApplication {
    public static void main(String[] args) {

        SpringApplication.run(PdfolioApplication.class, args);
    }

}
