/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.mongodb.repository;

import br.tech.desiderati.common.scanner.QRCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

@SuppressWarnings("unused")
public interface QRCodeRepository extends MongoRepository<QRCode, UUID> {

}
