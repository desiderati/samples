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
package br.tech.desiderati.sample.storage.controller;

import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.FileMetadata;
import br.tech.desiderati.sample.storage.service.StorageServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/diff")
public class UploadController {

    private final StorageServiceImpl storageService;

    @Autowired
    public UploadController(StorageServiceImpl storageService) {
        this.storageService = storageService;
    }

    /**
     * Here is a point of improvement that I did. Instead of dealing with Base64,
     * I decided to deal with Data Streaming directly. See the reason below:
     * <pre>
     * <i>"The traditional API assumes that file items must be stored somewhere
     * before they are actually accessable by the user. Spring Boot's default
     * {@link org.springframework.web.multipart.MultipartResolver} interface
     * handles the uploading of multipart files by storing them on the local
     * file system. Before the controller method is entered, the entire
     * multipart file must finish uploading to the server.
     * This approach is convenient, because it allows easy access to an
     * items contents. On the other hand, it is memory and time consuming."</i>
     * </pre>
     */
    @PostMapping("{fileId}/left")
    public void left(HttpServletRequest request, @PathVariable @Positive @NotNull Long fileId) throws IOException {
        FileMetadata fileMetadata = new FileMetadata(fileId, FileMetadata.SIDE.LEFT);
        storageService.store(fileMetadata, request.getInputStream());
    }

    @PostMapping("{fileId}/right")
    public void right(HttpServletRequest request, @PathVariable @Positive @NotNull Long fileId) throws IOException {
        FileMetadata fileMetadata = new FileMetadata(fileId, FileMetadata.SIDE.RIGTH);
        storageService.store(fileMetadata, request.getInputStream());
    }

    @GetMapping("{fileId}")
    public Diff diff(@PathVariable @Positive @NotNull Long fileId) throws IOException {
        return storageService.diff(fileId);
    }
}
