/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate.service;

import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service("trackService")
public class TrackServiceImpl implements TrackService {

    private static final AtomicLong counter = new AtomicLong();

    private static List<Track> tracks;

    static {
        tracks = populateDummyTracks();
    }

    @Override
    public void saveTrack(Track track) {
        track.setId(counter.incrementAndGet());
        tracks.add(track);
    }

    @Override
    public void updateTrack(Track track) {
        int index = tracks.indexOf(track);
        tracks.set(index, track);
    }

    @Override
    public void deleteTrackById(Long id) {
        tracks.removeIf(track -> track.getId() != null && track.getId().equals(id));
    }

    @Override
    public Track findById(Long id) {
        for (Track track : tracks) {
            if (track.getId() != null && track.getId().equals(id)) {
                return track;
            }
        }
        return null;
    }

    @Override
    public List<Track> findByName(String name) {
        List<Track> tracksFound = new ArrayList<>();
        for (Track track : tracks) {
            if (track.getTrackname().contains(name)) {
                tracksFound.add(track);
            }
        }
        return tracksFound;
    }

    @Override
    public List<Track> findAllTracks() {
        return tracks;
    }

    private static List<Track> populateDummyTracks() {
        List<Track> tracks = new ArrayList<>();
        tracks.add(new Track(counter.incrementAndGet(), "A Long Time", "Mayer Hawthorne", 278));
        tracks.add(new Track(counter.incrementAndGet(), "Rehab", "Amy WineHouse", 217));
        tracks.add(new Track(counter.incrementAndGet(), "Mr. Wendal", "Arrested Development", 247));
        return tracks;
    }
}
