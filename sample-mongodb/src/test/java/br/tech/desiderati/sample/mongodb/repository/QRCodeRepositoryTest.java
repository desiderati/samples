/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.mongodb.repository;

import br.tech.desiderati.common.exception.ApplicationException;
import br.tech.desiderati.common.scanner.QRCode;
import br.tech.desiderati.common.scanner.QRCodeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class QRCodeRepositoryTest {

    @Autowired
    private QRCodeRepository qrCodeRepository;

    private UUID id;

    @BeforeEach
    void setUp() {
        QRCode qrCode = QRCodeBuilder.withContentAsUUID().build();
        qrCodeRepository.save(qrCode);
        assertNotNull(qrCode.getId());
        assertNotNull(qrCode.getContent());
        assertNotNull(qrCode.getImage());
        id = qrCode.getId();
    }

    @Test
    void testFetchData() {
        QRCode qrCode = qrCodeRepository.findById(id)
            .orElseThrow(() -> new ApplicationException("Invalid QR Code"));

        assertNotNull(qrCode);
        assertNotNull(qrCode.getContent());
        assertNull(qrCode.getImage()); // Transient

        Iterable<QRCode> qrCodes = qrCodeRepository.findAll();
        for (QRCode qrCodeTmp : qrCodes) {
            log.info("UUID = " + qrCodeTmp.getId());
        }
    }

    @AfterEach
    void tearDown() {
        qrCodeRepository.deleteAll();
    }
}