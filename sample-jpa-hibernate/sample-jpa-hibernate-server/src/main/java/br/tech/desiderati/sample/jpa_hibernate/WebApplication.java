/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate;

import br.tech.desiderati.common.configuration.annotation.CustomSpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@CustomSpringBootApplication(componentScan = {"br.tech.desiderati.sample", "br.tech.desiderati.common"})
public class WebApplication {

    private static final String EXTERNAL_PROPERTIES = "/opt/app/config/sample/jpa-hibernate-server.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class)
            .properties("spring.config.location=file:" + EXTERNAL_PROPERTIES)
            .run(args);
    }
}
