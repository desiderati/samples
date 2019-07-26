/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate.service;

import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface TrackService {

    void saveTrack(@Valid Track track);

    void updateTrack(@Valid Track track);

    void deleteTrackById(Long id);

    Track findById(Long id);

    List<Track> findByName(String name);

    List<Track> findAllTracks();

}
