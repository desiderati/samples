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

import {Injectable} from '@angular/core';
import {Observable, Observer} from 'rxjs';

// Imports atmosphere.js from LIB installed via NPM.
import * as Atmosphere from 'atmosphere.js';

import {Notification} from './notification';
import AtmosphereRequest = Atmosphere.Request;

@Injectable({
    providedIn: 'root',
})
export class AtmosphereService {

    private socket: any = null;

    // noinspection JSUnusedGlobalSymbols
    connect(
        url: string,
        customAtmosphereRequest?: CustomAtmosphereRequest
    ): Observable<string> {

        let self = this;

        function subscribe(atmosphereRequest: {}) {
            if (Atmosphere && Atmosphere.subscribe) {
                self.socket = Atmosphere.subscribe(atmosphereRequest);
            }
        }

        function unsubscribe() {
            if (Atmosphere && Atmosphere.unsubscribe) {
                Atmosphere.unsubscribe();
                self.socket = null;
            }
        }

        if (this.isInitialized()) {
            unsubscribe();
        }

        return new Observable<string>((observer: Observer<string>) => {
            function debugMsgIfEnabled(msg: string) {
                if (customAtmosphereRequest?.logLevel === 'debug') {
                    if (customAtmosphereRequest?.logFunction !== undefined) {
                        customAtmosphereRequest.logFunction(msg);
                    } else {
                        console.log(msg);
                    }
                }
            }

            // noinspection JSUnusedGlobalSymbols
            let atmosphereRequest = {
                // Mandatory parameters.
                url: url,

                // Default parameters.
                contentType: 'application/json',
                reconnectInterval: 5000, // In milliseconds.
                maxReconnectOnClose: 7,
                transport: 'websocket',
                fallbackTransport: 'long-polling',
                trackMessageLength: true,
                enableProtocol: true,
                logLevel: 'info',
                uuid: 0,

                // Operations.
                onOpen: (response: any) => {
                    const msg = 'Atmosphere connected using ' + response.transport + '!';
                    debugMsgIfEnabled(msg);

                    // Carry the UUID. This is required if you want to call
                    // subscribe(request) again.
                    atmosphereRequest.uuid = response.request.uuid;
                },

                onReopen: (_: undefined, response: any) => {
                    const msg = 'Atmosphere reconnected using ' + response.transport + '!';
                    debugMsgIfEnabled(msg);
                },

                onMessage: (response: any) => {
                    const message = response.responseBody;
                    let json = null;
                    try {
                        json = JSON.parse(message);
                    } catch (e) {
                        console.error('Notification doesn\'t look like a valid JSON: ', message);
                        observer.error('Notification doesn\'t look like a valid JSON! See console for more information.');
                        return;
                    }
                    observer.next(json.payload);
                },

                onClose: () => {
                    let msg = 'Atmosphere connection was closed!';
                    debugMsgIfEnabled(msg);
                },

                onError: (response: any) => {
                    console.error('Response Error:', response);
                    observer.error('Sorry, but there\'s some problem with your socket or the server is down!');
                },

                onTransportFailure: (errorMsg: string, request: any) => {
                    console.warn('Transport Failure:', errorMsg);

                    request.fallbackTransport = 'long-polling';  // Always forcing long-polling!
                    const msg = 'Sorry, but it wasn\'t possible to establish a connection using ' + request.transport + '! ' +
                        'Falling back to ' + request.fallbackTransport + '.';
                    debugMsgIfEnabled(msg);
                },

                onReconnect: (request: any) => {
                    let msg = 'Connection lost, trying to reconnect. ' +
                        'Trying to reconnect in ' + request.reconnectInterval + 'ms.';
                    debugMsgIfEnabled(msg);
                },

                onClientTimeout(request: any) {
                    const msg = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval + 'ms.';
                    debugMsgIfEnabled(msg);

                    setTimeout(() => {
                        subscribe(request);
                    }, request.reconnectInterval);
                }
            };

            if (customAtmosphereRequest !== undefined) {
                subscribe({...atmosphereRequest, ...customAtmosphereRequest});
            } else {
                subscribe(atmosphereRequest);
            }

            return () => unsubscribe();
        });
    }

    // noinspection JSUnusedGlobalSymbols
    sendNotification(notification: Notification): void {
        if (this.isInitialized()) {
            this.socket.push(JSON.stringify(notification));
        }
    }

    isInitialized() {
        return this.socket !== null;
    }
}

interface CustomAtmosphereRequest extends AtmosphereRequest {
    logFunction?: ((msg: string) => void) | undefined;
}
