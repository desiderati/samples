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

