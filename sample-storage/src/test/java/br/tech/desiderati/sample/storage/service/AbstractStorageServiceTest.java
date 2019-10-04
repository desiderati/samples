/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.service;

import br.tech.desiderati.common.test.MockitoExtension;
import br.tech.desiderati.sample.storage.Fixtures;
import br.tech.desiderati.sample.storage.repository.FileMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;

import java.nio.file.Path;

@Slf4j
@ExtendWith(MockitoExtension.class)
abstract class AbstractStorageServiceTest {

    protected StorageServiceImpl storageService;

    @Mock
    protected FileMetadataRepository fileMetadataRepositoryMock;

    @Mock
    protected Path rootLocationMock;

    @Mock(name = "localFilePath", answer = Answers.RETURNS_DEEP_STUBS)
    protected Path filePathMock;

    @BeforeAll
    public static void setupAll() {
        Fixtures.configure();
    }

    @BeforeEach
    public void setup() {
        storageService = new StorageServiceImpl(fileMetadataRepositoryMock, rootLocationMock);
    }
}
