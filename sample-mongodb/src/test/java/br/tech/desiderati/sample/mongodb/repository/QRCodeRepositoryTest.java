/*
 * Copyright (c) 2025 - Felipe Desiderati
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
package br.tech.desiderati.sample.mongodb.repository;

import dev.springbloom.core.exception.ApplicationException;
import dev.springbloom.scanner.QRCode;
import dev.springbloom.scanner.QRCodeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@SpringBootTest
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
            log.info("UUID = {}", qrCodeTmp.getId());
        }
    }

    @AfterEach
    void tearDown() {
        qrCodeRepository.deleteAll();
    }
}
