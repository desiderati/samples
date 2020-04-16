/*
 * Copyright (c) 2020 - Felipe Desiderati
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
package br.tech.desiderati.sample.storage.repository;

import br.tech.desiderati.sample.storage.domain.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, UUID> {

    Optional<FileMetadata> findFirstByFileIdAndSide(Long fileId, FileMetadata.SIDE side);

    @Query("SELECT fm FROM FileMetadata fm WHERE fm.sha1 = ?1 AND (fm.fileId <> ?2 OR fm.side <> ?3)")
    List<FileMetadata> findAllFileMetadaWithSameSha1ButDiffFileIdOrSide(String sha1, Long fileId, FileMetadata.SIDE side);

    default Optional<FileMetadata> findFirstFileMetadaWithSameSha1ButDiffFileIdOrSide(String sha1, Long fileId, FileMetadata.SIDE side) {
        List<FileMetadata> all = findAllFileMetadaWithSameSha1ButDiffFileIdOrSide(sha1, fileId, side);
        if (all == null || all.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(all.get(0));
        }
    }
}
