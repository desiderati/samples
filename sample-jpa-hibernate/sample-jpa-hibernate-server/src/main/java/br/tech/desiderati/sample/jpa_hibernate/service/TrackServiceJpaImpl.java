/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.jpa_hibernate.service;

import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

// Optamos por não usar um Repository apenas para simplificar o exemplo!
@Transactional
@Service("trackServiceJpa")
public class TrackServiceJpaImpl implements TrackService {

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
        Track track = entityManager.find(Track.class, id);
        return track;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> findByName(String trackname) {
        List<Track> tracksFound =
            entityManager.createQuery("SELECT t FROM Track t WHERE t.trackname LIKE :trackname")
                .setParameter("trackname", "%" + trackname + "%").getResultList();
        return tracksFound;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Track> findAllTracks() {
        List<Track> tracksFound =
            entityManager.createQuery("SELECT t FROM Track t ORDER BY t.trackname ASC")
                .getResultList();
        return tracksFound;
    }
}
