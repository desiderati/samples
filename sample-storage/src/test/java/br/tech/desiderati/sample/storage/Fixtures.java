/*
 * Copyright (c) 2019 - Felipe Desiderati
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
