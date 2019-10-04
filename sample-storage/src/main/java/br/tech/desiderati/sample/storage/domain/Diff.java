/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.storage.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Diff {

    private Boolean sameFile;
    private Boolean diffSize;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<Offset> offsets;

    public List<Offset> getOffsets() {
        if (offsets == null) {
            offsets = new ArrayList<>();
        }
        return offsets;
    }
}
