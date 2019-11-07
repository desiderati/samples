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
