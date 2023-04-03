/*
 * Copyright (c) 2023 - Felipe Desiderati
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package br.tech.desiderati.sample.storage.service;

import br.tech.desiderati.sample.storage.Fixtures;
import br.tech.desiderati.sample.storage.repository.FileMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
