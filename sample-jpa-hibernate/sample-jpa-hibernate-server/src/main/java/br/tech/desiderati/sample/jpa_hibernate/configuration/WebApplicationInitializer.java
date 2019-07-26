/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate.configuration;

import br.tech.desiderati.common.configuration.AbstractWebApplicationInitializer;
import br.tech.desiderati.sample.jpa_hibernate.WebApplication;

public class WebApplicationInitializer extends AbstractWebApplicationInitializer {

    @Override
    protected Class<WebApplication> getWebApplicationClass() {
        return WebApplication.class;
    }
}
