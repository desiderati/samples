/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage;

import br.tech.desiderati.sample.storage.domain.FileMetadata;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Fixtures {

    public static FileMetadata validFileMetadata123456;

    public static FileMetadata validFileMetadata1234567;

    public static void configure() {
        validFileMetadata123456 =
            FileMetadata.builder()
                .fileId(1L)
                .side(FileMetadata.SIDE.LEFT)
                .sha1("7c4a8d09ca3762af61e59520943dc26494f8941b")
                .localFilename("7c/4a/7c4a8d09ca3762af61e59520943dc26494f8941b")
                .completed(true)
                .build();

        validFileMetadata1234567 =
            FileMetadata.builder()
                .fileId(1L)
                .side(FileMetadata.SIDE.LEFT)
                .sha1("20eabe5d64b0e216796e834f52d61fd0b70332fc")
                .localFilename("20/ea/20eabe5d64b0e216796e834f52d61fd0b70332fc")
                .completed(true)
                .build();
    }
}
