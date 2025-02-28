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
package br.tech.desiderati.sample.jpa_hibernate.service;

import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements TrackService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void saveTrack(Track track) {
        entityManager.persist(track);
    }

    @Override
    public void updateTrack(Track track) {
        entityManager.merge(track);
    }

    @Override
    public void deleteTrackById(Long id) {
        entityManager.remove(findById(id));
    }

    @Override
    public Track findById(Long id) {
        return entityManager.find(Track.class, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> findByName(String trackName) {
        return entityManager.createQuery("SELECT t FROM Track t WHERE t.trackName LIKE :trackName")
            .setParameter("trackName", "%" + trackName + "%").getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> findAllTracks() {
        return entityManager.createQuery("SELECT t FROM Track t ORDER BY t.trackName ASC").getResultList();
    }
}
