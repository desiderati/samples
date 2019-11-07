/*
 * Copyright (c) 2019 - Felipe Desiderati
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

import br.tech.desiderati.common.exception.NotFoundRestApiException;
import br.tech.desiderati.sample.jpa_hibernate.controller.dto.TrackDTO;
import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import br.tech.desiderati.sample.jpa_hibernate.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/track")
public class TrackController {

    @Resource(name = "trackServiceJpa")
    private final TrackService trackService;

    private ModelMapper modelMapper;

    @Autowired
    public TrackController(TrackService trackService, ModelMapper modelMapper) {
        this.trackService = trackService;
        this.modelMapper = modelMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<TrackDTO> fetchAllTracks() {
        List<Track> tracks = trackService.findAllTracks();
        if (tracks.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrackDTO> trackDTOs = new ArrayList<>();
        for (Track track : tracks) {
            trackDTOs.add(modelMapper.map(track, TrackDTO.class));
        }
        return trackDTOs;
    }

    @RequestMapping(value = "/{trackname}", method = RequestMethod.GET)
    public List<TrackDTO> fetchTrackByName(@PathVariable("trackname") String trackname) {
        List<Track> tracks = trackService.findByName(trackname);
        if (tracks.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrackDTO> trackDTOs = new ArrayList<>();
        for (Track track : tracks) {
            trackDTOs.add(modelMapper.map(track, TrackDTO.class));
        }
        return trackDTOs;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createTrack(@RequestBody @Valid TrackDTO trackDTO) {
        log.info("Creating Track " + trackDTO.getTrackname());
        trackService.saveTrack(modelMapper.map(trackDTO, Track.class));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TrackDTO updateTrack(@PathVariable("id") Long id, @RequestBody @Valid TrackDTO trackDTO) {
        log.info("Updating Track with id " + id);

        Track currentTrack = trackService.findById(id);
        if (currentTrack == null) {
            log.error("Track with id " + id + " not found");
            throw new NotFoundRestApiException("track_not_found_exception", id);
        }

        modelMapper.map(trackDTO, currentTrack);
        trackService.updateTrack(currentTrack);
        return modelMapper.map(currentTrack, TrackDTO.class);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTrack(@PathVariable("id") long id) {
        log.info("Fetching & Deleting Track with id " + id);

        Track track = trackService.findById(id);
        if (track == null) {
            log.error("Unable to delete. Track with id " + id + " not found");
            throw new NotFoundRestApiException("track_not_found_exception", id);
        }

        trackService.deleteTrackById(id);
    }
}
