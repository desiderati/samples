/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage;

import br.tech.desiderati.common.configuration.annotation.CustomSpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Não há necessidade de colocar o atributo ComponentScan, já que os pacotes da aplicação são os padrões.
 */
@CustomSpringBootApplication
public class WebApplication {

    private static final String EXTERNAL_PROPERTIES = "/opt/desiderati-java-app/config/samples/storage.properties";

    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class)
            .properties("spring.config.location=file:" + EXTERNAL_PROPERTIES)
            .run(args);
    }
}
