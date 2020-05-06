/*
 * Copyright (c) 2020 - Felipe Desiderati
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
import {Util} from './util';

import * as Atmosphere from 'atmosphere.js';
import * as $ from 'jquery';

export class NotificationService {

    private socket: typeof Atmosphere = Atmosphere;
    private subscription = null;

    // noinspection JSUnusedGlobalSymbols,JSUnusedLocalSymbols
    private atmosphereRequest = {

        // Mandatory parameters.
        url: '',
        notificationCallback: (msg: string) => {
        },

        statusCallback: (msg: string) => {
        },

        subscribeOnTimeout: (request: any) => {
        },

        // Default parameters.
        contentType: 'application/json',
        reconnectInterval: 5000, // In milliseconds.
        maxReconnectOnClose: 7,
        transport: 'websocket',
        fallbackTransport: 'long-polling',
        trackMessageLength: true,
        logLevel: 'debug',

        // Operations.
        onOpen(response) {
            const msg = 'Atmosphere connected using ' + response.transport + '!';
            console.log(msg);
            this.statusCallback(msg);

            // Carry the UUID. This is required if you want to call
            // subscribe(request) again.
            // noinspection TypeScriptUnresolvedVariable
            this.uuid = response.request.uuid;
        },

        onReopen(request, response) {
            const msg = 'Atmosphere reconnected using ' + response.transport + '!';
            console.log(msg);
            this.statusCallback(msg);
        },

        onMessage(response): (response) => void {
            // noinspection TypeScriptUnresolvedVariable
            const message = response.responseBody;
            let json = null;
            try {
                json = JSON.parse(message);
            } catch (e) {
                console.error('This doesn\'t look like a valid JSON: ', message);
                return;
            }

            this.notificationCallback(json.message);
        },

        onClose() {
            const msg = 'Atmosphere connection was closed!';
            console.log(msg);
            this.statusCallback(msg);
        },

        onError(response) {
            console.error('Response Error:', response);
            this.statusCallback('Sorry, but there\'s some problem with your socket or the server is down!');
        },

        onTransportFailure(errorMsg, request) {
            request.fallbackTransport = 'long-polling';  // Always forcing long-polling!
            const msg = 'Sorry, but it wasn\'t possible to establish a connection using ' + request.transport + '! ' +
                'Falling back to ' + request.fallbackTransport + '.';
            console.log(msg);
            this.statusCallback(msg);
        },

        onReconnect(request) {
            const msg = 'Connection lost, trying to reconnect. Trying to reconnect in ' + request.reconnectInterval + 'ms.';
            console.log(msg);
            this.statusCallback(msg);
        },

        onClientTimeout(request) {
            const msg = 'Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval + 'ms.';
            console.log(msg);
            this.statusCallback(msg);

            setTimeout(() => {
                this.subscribeOnTimeout(request);
            }, request.reconnectInterval);
        }
    };

    constructor(...args: any[]) {
        if (args.length === 0 || args.length > 3) {
            throw new Error('Invalid arguments!');
        }

        let params = null;
        let notificationCallback;
        let statusCallback;

        if (args.length === 1) {
            if (!$.isFunction(args[0])) {
                throw new Error('Argument must be a valid (Notification) callback function!');
            }

            notificationCallback = args[0];
        }

        if (args.length === 2) {
            if (Util.isJson(args[0]) && $.isFunction(args[1])) {
                params = args[0];
                notificationCallback = args[1];

            } else if ($.isFunction(args[0]) && $.isFunction(args[1])) {
                notificationCallback = args[0];
                statusCallback = args[1];

            } else {
                throw new Error('First argument must be a valid JSON Object containing the request parameters and ' +
                    'second argument must be a valid (Notification) callback function! Or...  First argument must be ' +
                    'a valid (Notification) callback function and second argument must be a valid (Status) callback function!');
            }
        }

        if (args.length === 3) {
            const validArguments = Util.isJson(args[0]) && $.isFunction(args[1]) && $.isFunction(args[2]);
            if (!validArguments) {
                throw new Error('First argument must be a valid JSON Object containing the request parameters and ' +
                    'second argument must be a valid (Notification) callback function and third argument must be ' +
                    'a valid (Status) callback function!');
            }

            params = args[0];
            notificationCallback = args[1];
            statusCallback = args[2];
        }

        if (!statusCallback) {
            if (window.console) {
                statusCallback = window.console.log;
            } else {
                statusCallback = () => {
                    // Do nothing! Just to avoid NPE.
                };
            }
        }

        if (!!params) {
            for (const key of Object.keys(params)) {
                this.atmosphereRequest[key] = params[key];
            }
        }
        this.atmosphereRequest.notificationCallback = notificationCallback;
        this.atmosphereRequest.statusCallback = statusCallback;
    }

    subscribe(url: string) {
        if (!url) {
            throw new Error('Url parameter cannot be null or undefined!');
        }

        if (this.subscription != null) {
            this.unsubscribe();
        }

        this.atmosphereRequest.url = url;
        this.subscription = this.socket.subscribe(this.atmosphereRequest);
        this.atmosphereRequest.subscribeOnTimeout = (request: any) => {
            this.subscription = this.socket.subscribe(request);
        };
    }

    unsubscribe() {
        this.socket.unsubscribeUrl(this.atmosphereRequest.url);
        this.subscription = null;
    }

    push(msg: string) {
        this.subscription.push(JSON.stringify({
            message: msg
        }));
    }

    isInitialized() {
        return this.subscription != null;
    }
}
