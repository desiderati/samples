/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.service;

import br.tech.desiderati.common.exception.IllegalArgumentApplicationException;
import br.tech.desiderati.common.exception.ResourceNotFoundApplicationException;
import br.tech.desiderati.sample.storage.configuration.StorageProperties;
import br.tech.desiderati.sample.storage.domain.Diff;
import br.tech.desiderati.sample.storage.domain.FileMetadata;
import br.tech.desiderati.sample.storage.domain.Offset;
import br.tech.desiderati.sample.storage.repository.FileMetadataRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

import static br.tech.desiderati.common.exception.ThrowingConsumer.uncheckedConsumer;
import static br.tech.desiderati.common.exception.ThrowingRunnable.uncheckedRunnable;

@Slf4j
@Service
@Validated
public class StorageServiceImpl implements StorageService {

    private static final int BUFFER_SIZE = 8192;

    private final FileMetadataRepository fileMetadataRepository;

    private final Path rootLocation;

    @Autowired
    public StorageServiceImpl(FileMetadataRepository fileMetadataRepository, StorageProperties properties) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.rootLocation = Paths.get(properties.getRootLocation());
    }

    /**
     * Constructor used by tests only.
     */
    StorageServiceImpl(FileMetadataRepository fileMetadataRepository, Path rootLocation) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.rootLocation = rootLocation;
    }

    @Override
    @Transactional
    public void store(@Valid FileMetadata fileMetadata, @NotNull InputStream inputStream) {
        FileMetadata newFileMetadata =
            fileMetadataRepository.findFirstByFileIdAndSide(fileMetadata.getFileId(), fileMetadata.getSide())
                .orElse(null);
        validateFileAlreadyUploaded(newFileMetadata);
        if (newFileMetadata == null) {
            newFileMetadata = fileMetadata;
        }

        try {
            deletePartiallyUploadedFile(newFileMetadata);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            writeFileAndValidateDuplicates(newFileMetadata, inputStream, digest);
        } catch (Exception e) {
            throw new StorageException(
                "Failed to store file: '" + newFileMetadata.getFileId() + "'.", e);
        } finally {
            fileMetadataRepository.save(newFileMetadata);
        }
    }

    private void validateFileAlreadyUploaded(FileMetadata fileMetadata) {
        if (fileMetadata != null && fileMetadata.getCompleted()) {
            throw new IllegalArgumentApplicationException("file_already_uploaded_excpetion");
        }
    }

    private void deletePartiallyUploadedFile(FileMetadata fileMetadata) throws IOException {
        Path tmpLocalFilePath = rootLocation.resolve(fileMetadata.getTempFilename());
        Files.deleteIfExists(tmpLocalFilePath);
    }

    /**
     * Here is another point of improvement.
     * <br>
     * Regardless of whether or not an equal file already exists, we save the file on disk.
     * Subsequently, after calculating SHA1, I verify if there is a file (same content but with different Id)
     * already written to disk. If so, I removed the newly created file (physically) and associated
     * the metadata with the existing one.
     */
    private void writeFileAndValidateDuplicates(FileMetadata fileMetadata, InputStream inputStream,
                                                MessageDigest digest) throws IOException {

        // Creates temp folder if not exists!
        Path tmpLocalFilePath = rootLocation.resolve(fileMetadata.getTempFilename());
        Files.createDirectories(tmpLocalFilePath.getParent());

        // Saves file on temp folder.
        OpenOption openOption = StandardOpenOption.CREATE_NEW;
        OutputStream outputStream =
            tmpLocalFilePath.getFileSystem().provider().newOutputStream(
                tmpLocalFilePath, openOption, StandardOpenOption.WRITE);
        String sha1 = writeFile(inputStream, outputStream, digest);
        fileMetadata.setSha1(sha1);

        fileMetadataRepository.findFirstFileMetadaWithSameSha1ButDiffFileIdOrSide(sha1, fileMetadata.getFileId(), fileMetadata.getSide())
            .ifPresentOrElse(
                uncheckedConsumer(sha1FileMetadata -> {
                    // If file is duplicated, don't stores it. Just use the same reference. (Save space on disk)
                    fileMetadata.replaceLocalFilenameWith(sha1FileMetadata);
                    Files.deleteIfExists(tmpLocalFilePath);
                }),
                uncheckedRunnable(() -> {
                    // Moves from temp folder to the destination folter.
                    Path localFilePath = rootLocation.resolve(fileMetadata.getLocalFilename());
                    Files.createDirectories(localFilePath.getParent());
                    Files.move(tmpLocalFilePath, localFilePath, StandardCopyOption.REPLACE_EXISTING);
                }));

        fileMetadata.setCompleted(true);
    }

    private String writeFile(InputStream inputStream, OutputStream outputStream,
                             MessageDigest digest) throws IOException {

        try (DigestOutputStream digestOutputStream = new DigestOutputStream(outputStream, digest)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digestOutputStream.write(buffer, 0, bytesRead);
                digestOutputStream.flush();
            }
            return Hex.encodeHexString(digestOutputStream.getMessageDigest().digest());
        }
    }

    @Override
    @Transactional
    public Diff diff(@Positive @NotNull Long fileId) throws IOException {
        FileMetadata leftFileMetadata =
            fileMetadataRepository.findFirstByFileIdAndSide(fileId, FileMetadata.SIDE.LEFT)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundApplicationException("left_side_not_found_excpetion");
                });

        FileMetadata rightFileMetadata =
            fileMetadataRepository.findFirstByFileIdAndSide(fileId, FileMetadata.SIDE.RIGTH)
                .orElseThrow(() -> {
                    throw new ResourceNotFoundApplicationException("right_side_not_found_excpetion");
                });

        Diff diff = new Diff();
        if (leftFileMetadata.getSha1().equalsIgnoreCase(rightFileMetadata.getSha1())) {
            diff.setSameFile(true);
            return diff;
        }

        Path leftFilePath = rootLocation.resolve(leftFileMetadata.getLocalFilename());
        Path rigthFilePath = rootLocation.resolve(rightFileMetadata.getLocalFilename());

        try (FileChannel leftChannel = new RandomAccessFile(leftFilePath.toFile(), "r").getChannel();
             FileChannel rightChannel = new RandomAccessFile(rigthFilePath.toFile(), "r").getChannel()) {

            if (leftChannel.size() != rightChannel.size()) {
                diff.setDiffSize(true);
                return diff;
            }

            long size = leftChannel.size();
            ByteBuffer m1 = leftChannel.map(FileChannel.MapMode.READ_ONLY, 0L, size);
            ByteBuffer m2 = rightChannel.map(FileChannel.MapMode.READ_ONLY, 0L, size);

            Offset diffOffset = null;
            for (int position = 0; position < size; position++) {
                boolean hasDiffOffset = (diffOffset != null);
                if (m1.get(position) != m2.get(position)) {
                    if (!hasDiffOffset) {
                        diffOffset = new Offset(position);
                    } else {
                        diffOffset.incrementLength();
                    }
                } else {
                    if (hasDiffOffset) {
                        diff.getOffsets().add(diffOffset);
                        diffOffset = null;
                    }
                }
            }

            if (diffOffset != null) {
                diff.getOffsets().add(diffOffset);
                diffOffset = null;
            }
            return diff;
        }
    }
}
