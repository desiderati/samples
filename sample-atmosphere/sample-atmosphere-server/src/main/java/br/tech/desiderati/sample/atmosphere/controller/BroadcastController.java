/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
package br.tech.desiderati.sample.atmosphere.controller;

import br.tech.desiderati.common.notification.NotificationService;
import br.tech.desiderati.sample.atmosphere.controller.dto.BroadcastDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/broadcast")
public class BroadcastController {

    private final NotificationService notificationService;

    @Autowired
    public BroadcastController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public void broadcast(@RequestBody @Valid BroadcastDTO broadcastDTO) {
        if (broadcastDTO.getResourceId() != null) {
            notificationService.broadcastToSpecificResource(broadcastDTO.getResourceId(), broadcastDTO.getMessage());
        } else if (broadcastDTO.getBroadcastId() != null) {
            notificationService.broadcastToSpecificBroadcaster(broadcastDTO.getBroadcastId(), broadcastDTO.getMessage());
        } else {
            notificationService.broadcast(broadcastDTO.getMessage());
        }
    }
}

