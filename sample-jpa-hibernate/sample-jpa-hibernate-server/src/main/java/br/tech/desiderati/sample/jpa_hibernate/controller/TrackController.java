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
package br.tech.desiderati.sample.jpa_hibernate.controller;

import br.tech.desiderati.sample.jpa_hibernate.controller.dto.TrackDTO;
import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import br.tech.desiderati.sample.jpa_hibernate.service.TrackService;
import dev.springbloom.jms.AsyncMessagePublisher;
import dev.springbloom.web.rest.exception.NotFoundRestApiException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/track")
public class TrackController {

    private final TrackService trackService;

    private final ModelMapper modelMapper;

    private final AsyncMessagePublisher trackMessagePublisher;

    @Autowired
    public TrackController(
        AsyncMessagePublisher trackMessagePublisher,
        TrackService trackService,
        ModelMapper modelMapper
    ) {
        this.trackService = trackService;
        this.modelMapper = modelMapper;
        this.trackMessagePublisher = trackMessagePublisher;
    }

    @GetMapping
    public List<TrackDTO> fetchAllTracks() {
        List<Track> tracks = trackService.findAllTracks();
        return getTrackDTOS(tracks);
    }

    @GetMapping(value = "/{trackName}")
    public List<TrackDTO> fetchTrackByName(@PathVariable("trackName") String trackName) {
        List<Track> tracks = trackService.findByName(trackName);
        return getTrackDTOS(tracks);
    }

    @NotNull
    private List<TrackDTO> getTrackDTOS(List<Track> tracks) {
        if (tracks.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrackDTO> trackDTOs = new ArrayList<>();
        for (Track track : tracks) {
            trackDTOs.add(modelMapper.map(track, TrackDTO.class));
        }
        return trackDTOs;
    }

    @PostMapping
    public void createTrack(@RequestBody @Valid TrackDTO trackDTO) {
        log.info("Creating Track '{}'", trackDTO.getTrackName());

        // Send the message to the queue.
        trackMessagePublisher.publish(modelMapper.map(trackDTO, Track.class));
    }

    @PutMapping(value = "/{id}")
    public TrackDTO updateTrack(@PathVariable("id") Long id, @RequestBody @Valid TrackDTO trackDTO) {
        log.info("Updating Track with id '{}'", id);

        Track currentTrack = trackService.findById(id);
        if (currentTrack == null) {
            log.error("Track with id '{}' not found", id);
            throw new NotFoundRestApiException("track_not_found_exception", id);
        }

        modelMapper.map(trackDTO, currentTrack);
        trackService.updateTrack(currentTrack);
        return modelMapper.map(currentTrack, TrackDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteTrack(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Track with id {}", id);

        Track track = trackService.findById(id);
        if (track == null) {
            log.error("Unable to delete. Track with id {} not found", id);
            throw new NotFoundRestApiException("track_not_found_exception", id);
        }

        trackService.deleteTrackById(id);
    }
}
