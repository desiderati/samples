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
package br.tech.desiderati.sample.storage.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.File;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"fileId", "side"})

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Needed by the Builder!!!

@Entity
@Table(name = "file_metadata",
    uniqueConstraints = @UniqueConstraint(columnNames = {"fileId", "side"}),
    indexes = @Index(columnList = "sha1")
)
/**
 * I could have used a Composite Primary Key, but I decided to keep this way for simplicity.
 *
 * @see EmbeddedId
 */
public class FileMetadata extends AbstractPersistable<Long> {

    public enum SIDE {
        LEFT, RIGTH
    }

    @SuppressWarnings("squid:S2637") // "@NonNull" values should not be set to null
    public FileMetadata(Long fileId, SIDE side) {
        setFileId(fileId);
        setSide(side);
    }

    @Positive
    @NotNull
    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private Long fileId;

    @NotNull
    @Column(nullable = false)
    @Setter(AccessLevel.PRIVATE)
    private SIDE side;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private String localFilename;

    @Column(nullable = false, length = 40)
    private String sha1;

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean completed = false;

    public String getTempFilename() {
        if (fileId == null || side == null) {
            throw new IllegalStateException("FileId and Side can not be null!!!");
        }
        return side.name().toLowerCase() + File.separator + fileId;
    }

    public String getLocalFilename() {
        if (localFilename == null) {
            if (sha1 == null) {
                throw new IllegalStateException("Sha1 can not be null!!!");
            }

            String firstDir = sha1.substring(0, 2);
            String secondDir = sha1.substring(2, 4);
            localFilename = firstDir + File.separator + secondDir + File.separator + sha1;
        }
        return localFilename;
    }

    public void replaceLocalFilenameWith(FileMetadata fileMetadata) {
        if (fileMetadata != null && fileMetadata.getLocalFilename() != null) {
            localFilename = fileMetadata.getLocalFilename();
        }
    }
}