/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.configuration;

import br.tech.desiderati.common.data.jpa.DefaultPhysicalNamingStrategy;

/**
 * Need to set property:
 * <p>
 * spring.jpa.hibernate.naming.physical-strategy=br.tech.desiderati.sample.storage.configuration.StoragePhysicalNamingStrategy
 * <p>
 * Inside <b>application.properties</b> file.
 */
@SuppressWarnings("unused")
public class StoragePhysicalNamingStrategy extends DefaultPhysicalNamingStrategy {

    @Override
    protected String getPrefix() {
        return "stg";
    }
}
