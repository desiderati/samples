/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.controller;

import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.FileMetadata;
import br.tech.desiderati.sample.storage.service.StorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
