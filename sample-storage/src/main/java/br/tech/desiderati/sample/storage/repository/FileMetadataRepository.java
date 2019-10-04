/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.repository;

import br.tech.desiderati.sample.storage.domain.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
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
