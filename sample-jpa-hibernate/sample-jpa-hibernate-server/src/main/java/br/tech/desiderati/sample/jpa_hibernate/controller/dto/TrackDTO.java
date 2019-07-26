/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate.controller.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class TrackDTO implements Serializable {

    private Long id;

    @NotBlank(message = "{track.author.notblank}")
    private String author;

    @NotBlank(message = "{track.trackname.notblank}")
    private String trackname;

    @Min(value = 10, message = "{track.duration.minvalue}")
    @NotNull(message = "{track.duration.notnull}")
    private Integer duration;

}
