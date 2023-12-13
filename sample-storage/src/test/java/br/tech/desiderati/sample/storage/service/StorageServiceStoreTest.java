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
import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.FileMetadata;
import io.herd.common.exception.ResourceNotFoundApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Unit Tests.
 * <p>
 * Those tests don't cover all code. I've only created some of them to show how to test
 * the Service Layer. Probably I shouldn't have to be lazy, but come on guys, it is just
 * a sample, not a real system.
 */
@Slf4j
class StorageServiceStoreTest extends AbstractStorageServiceTest {

    @Test
    void diffShouldThrowExceptionBecauseNoLeftSide() throws IOException {
        when(fileMetadataRepositoryMock.findFirstByFileIdAndSide(anyLong(), any()))
            .thenReturn(Optional.empty());

        try {
            storageService.diff(1L);
            fail("diffShouldThrowExceptionBecauseNoLeftSide");
        } catch (ResourceNotFoundApplicationException ex) {
            assertEquals("left_side_not_found_excpetion", ex.getMessage());
        }
    }

    @Test
    void diffShouldThrowExceptionBecauseNoRigthSide() throws IOException {
        FileMetadata leftFileMetadata = Fixtures.validFileMetadata123456;
        when(fileMetadataRepositoryMock.findFirstByFileIdAndSide(anyLong(), any()))
            .thenReturn(Optional.of(leftFileMetadata))
            .thenReturn(Optional.empty());

        try {
            storageService.diff(1L);
            fail("diffShouldThrowExceptionBecauseNoRigthSide");
        } catch (ResourceNotFoundApplicationException ex) {
            assertEquals("right_side_not_found_excpetion", ex.getMessage());
        }
    }

    @Test
    void diffShouldReturnSameFile() throws IOException {
        FileMetadata leftFileMetadata = Fixtures.validFileMetadata123456;
        FileMetadata rightFileMetadata = Fixtures.validFileMetadata123456;
        when(fileMetadataRepositoryMock.findFirstByFileIdAndSide(anyLong(), any()))
            .thenReturn(Optional.of(leftFileMetadata))
            .thenReturn(Optional.of(rightFileMetadata));

        Diff diff = storageService.diff(1L);
        assertTrue(diff.getSameFile());
    }

    @Test
    void diffShouldThrowExceptionBecauseInvalidLeftRootLocation() throws IOException {
        FileMetadata leftFileMetadata = Fixtures.validFileMetadata123456;
        FileMetadata rightFileMetadata = Fixtures.validFileMetadata1234567;
        when(fileMetadataRepositoryMock.findFirstByFileIdAndSide(anyLong(), any()))
            .thenReturn(Optional.of(leftFileMetadata))
            .thenReturn(Optional.of(rightFileMetadata));

        try {
            when(rootLocationMock.resolve(leftFileMetadata.getLocalFilename()))
                .thenThrow(new InvalidPathException("File", "Invalid filename"));

            storageService.diff(1L);
            fail("diffShouldThrowExceptionBecauseInvalidLeftRootLocation");
        } catch (InvalidPathException ex) {
            assertEquals("Invalid filename: File", ex.getMessage());
        }
    }

    @Test
    void diffShouldThrowExceptionBecauseInvalidRightRootLocation() throws IOException {
        FileMetadata leftFileMetadata = Fixtures.validFileMetadata123456;
        FileMetadata rightFileMetadata = Fixtures.validFileMetadata1234567;
        when(fileMetadataRepositoryMock.findFirstByFileIdAndSide(anyLong(), any()))
            .thenReturn(Optional.of(leftFileMetadata))
            .thenReturn(Optional.of(rightFileMetadata));

        try {
            when(rootLocationMock.resolve(leftFileMetadata.getLocalFilename()))
                .thenReturn(filePathMock);
            when(rootLocationMock.resolve(rightFileMetadata.getLocalFilename()))
                .thenThrow(new InvalidPathException("File", "Invalid filename"));

            storageService.diff(1L);
            fail("diffShouldThrowExceptionBecauseInvalidRightRootLocation");
        } catch (InvalidPathException ex) {
            assertEquals("Invalid filename: File", ex.getMessage());
        }
    }
}
