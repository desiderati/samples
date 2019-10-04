/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.configuration;

import br.tech.desiderati.common.configuration.AbstractSwaggerConfiguration;
import org.springframework.boot.SpringBootConfiguration;

@SpringBootConfiguration
public class StorageSwaggerConfiguration extends AbstractSwaggerConfiguration {

    @Override
    protected String packagesToScan() {
        return "br.tech.desiderati.sample.storage.controller";
    }
}
