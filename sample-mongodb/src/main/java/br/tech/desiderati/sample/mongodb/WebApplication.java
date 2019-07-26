/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.mongodb;

import br.tech.desiderati.common.configuration.annotation.CustomSpringBootApplication;
import org.springframework.boot.SpringApplication;

@CustomSpringBootApplication(componentScan = {"br.tech.desiderati.sample", "br.tech.desiderati.common"})
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
