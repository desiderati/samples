'use strict';

App.controller('AtmosphereController', ['$scope',
    function ($scope) {

        let atmosphereRequest = null;
        let subscription = null;
        let socket = atmosphere;
        let self = this;

        self.notification = {
            user: "desiderati",
            message: "Testing message sending via Pure Javascript!"
        };

        self.initialize = function () {
            if (atmosphereRequest != null) {
                socket.unsubscribeUrl(atmosphereRequest.url);
                subscription = '';
            }

            atmosphereRequest = new AtmosphereRequest(
                document.location.toString() + 'samples_notification/' + self.notification.user,
                subscription,
                function (msg) {
                    self.printMessage(msg)
                },
                function (msg) {
                    self.printMessage(msg)
                });

            subscription = socket.subscribe(atmosphereRequest);

            $scope.$on('$destroy', function () {
                socket.unsubscribeUrl(atmosphereRequest.url);
                subscription = '';
            });
        };

        self.sendMessage = function () {
            console.log("Sending message '" + self.notification.message + "' to server!");
            if (atmosphereRequest == null) {
                self.printMessage("Atmosphere not initialized!");
                return;
            }

            subscription.push(socket.util.stringifyJSON({
                message: self.notification.message
            }));
        };

        self.printMessage = function(message) {
            $('#notificationContainer').show();
            $('<li/>').html(message).appendTo('#notificationBoard');
        };

        // Link: https://github.com/Atmosphere/atmosphere/wiki/atmosphere.js-API
        function AtmosphereRequest(url, subscription, notificationHandler, notificationConsole) {

            this["url"] = url;
            this["contentType"] = 'application/json';
            this["reconnectInterval"] = 5000;
            this["maxReconnectOnClose"] = 7;
            this["transport"] = 'websocket';
            this["fallbackTransport"] = 'long-polling';
            this["trackMessageLength"] = true;
            this["logLevel"] = 'debug';

            this["onOpen"] = function (response) {
                let msg = "Atmosphere connected using " + response.transport + "!";
                console.log(msg);
                notificationConsole(msg);

                // Carry the UUID. This is required if you want to call
                // subscribe(request) again.
                this.uuid = response.request.uuid;
            };

            this["onReopen"] = function (response) {
                let msg = "Atmosphere reconnected using " + response.transport + "!";
                console.log(msg);
                notificationConsole(msg);
            };

            this["onMessage"] = function (response) {
                let message = response.responseBody;
                let json = null;
                try {
                    json = socket.util.parseJSON(message);
                } catch (e) {
                    console.log("This doesn't look like a valid JSON: ", message);
                    return;
                }

                console.log("Response JSON:", json);
                notificationHandler(json.message);
            };

            this["onClose"] = function () {
                console.log("Atmosphere connection was closed!");
            };

            this["onError"] = function (response) {
                console.log("Response Error:", response);
                notificationConsole("Sorry, but there's some problem with your socket " +
                    "or the server is down!");
            };

            this["onTransportFailure"] = function (errorMsg, request) {
                console.log("Request Error:", request);
                request.fallbackTransport = "long-polling";
            };

            this["onReconnect"] = function (request) {
                let msg = "Connection lost, trying to reconnect. " +
                    "Trying to reconnect in " + request.reconnectInterval + "ms.";

                console.log(msg);
                notificationConsole(msg);
            };

            this["onClientTimeout"] = function (request) {
                let msg = "Client closed the connection after a timeout. " +
                    "Reconnecting in " + request.reconnectInterval + "ms.";

                console.log(msg);
                notificationConsole(msg);

                setTimeout(function () {
                    subscription = socket.subscribe(request);
                }, request.reconnectInterval);
            }
        }
    }]
);

