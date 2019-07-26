/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.mongodb.service;

import br.tech.desiderati.common.scanner.QRCode;
import br.tech.desiderati.common.scanner.QRCodeBuilder;
import br.tech.desiderati.sample.mongodb.repository.QRCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QRCodeService {

    private final QRCodeRepository qrCodeRepository;

    @Autowired
    public QRCodeService(QRCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    public QRCode generateQRCodeUsingUUID() {
        QRCode qrCode = QRCodeBuilder.withContentAsUUID().build();
        qrCodeRepository.save(qrCode);
        return qrCode;
    }
}
