/*
 * Copyright (c) 2024 - Felipe Desiderati
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
package br.tech.desiderati.sample.jpa_hibernate.listener;

import br.tech.desiderati.sample.jpa_hibernate.domain.Track;
import br.tech.desiderati.sample.jpa_hibernate.service.TrackService;
import io.herd.common.exception.ApplicationException;
import io.herd.common.exception.IllegalArgumentApplicationException;
import io.herd.common.jms.AbstractAsyncMessageListener;
import io.herd.common.web.notification.client.NotificationClient;
import jakarta.jms.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrackMessageListener extends AbstractAsyncMessageListener<Track> {

    private final NotificationClient notificationClient;

    private final TrackService trackService;

    public TrackMessageListener(NotificationClient notificationClient, TrackService trackService) {
        this.notificationClient = notificationClient;
        this.trackService = trackService;
    }

    @Override
    @SneakyThrows
    protected void receive(Track track) {
        // Simulating a long process of converting audio tracks to a long set of formats.
        Thread.sleep(3000);

        if (trackService.findByName(track.getTrackName()).isEmpty()) {
            trackService.saveTrack(track);
            notificationClient.broadcastToAll("Track saved successfully.");
        } else {
            throw new IllegalArgumentApplicationException("Track already exists!");
        }
    }

    @Override
    protected void handleApplicationException(
        Message jmsMessage,
        Track message,
        ApplicationException applicationException
    ) {
        super.handleApplicationException(jmsMessage, message, applicationException);
        notificationClient.broadcastToAll(applicationException.getMessage());
    }
}
