/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.domain;

import lombok.*;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class Offset {

    @NonNull
    private long position;
    private long length = 1L;

    public void incrementLength() {
        length++;
    }
}
