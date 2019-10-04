/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.service;

import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.FileMetadata;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;

/**
 * Probably I should have separated both methods in different classes because it is probably
 * breaking the SOLID Principles. In specific, the Single Responsibility Principle. Although
 * it is a simple example, I decided to maintain both methods together.
 */
public interface StorageService {

    @Transactional
    void store(@Valid FileMetadata fileMetadata, @NotNull InputStream inputStream);

    @Transactional
    Diff diff(@Positive @NotNull Long fileId) throws IOException;

}
