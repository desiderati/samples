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
(function (app) {
  'use strict';

  const apiUrl = 'http://localhost:9090/sample-atmosphere/'

  app.controller('AtmosphereController', ['$scope',
    function ($scope) {

      // Link: https://github.com/Atmosphere/atmosphere/wiki/atmosphere.js-API
      function AtmosphereRequest(url, subscription, notificationHandler) {

        this['url'] = url;
        this['contentType'] = 'application/json';
        this['reconnectInterval'] = 5000;
        this['maxReconnectOnClose'] = 7;
        this['transport'] = 'websocket';
        this['fallbackTransport'] = 'long-polling';
        this['trackMessageLength'] = true;
        this['logLevel'] = 'debug';

        this['onOpen'] = function (response) {
          let msg = 'Atmosphere connected using ' + response.transport + '!';
          console.log(msg);
          notificationHandler(msg);

          // Carry the UUID. This is required if you want to call
          // subscribe(request) again.
          this.uuid = response.request.uuid;
        };

        this['onReopen'] = function (response) {
          let msg = 'Atmosphere reconnected using ' + response.transport + '!';
          console.log(msg);
          notificationHandler(msg);
        };

        this['onMessage'] = function (response) {
          let message = response.responseBody;
          let json = null;
          try {
            json = JSON.parse(message);
          } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message);
            return;
          }

          // noinspection JSUnresolvedReference
          notificationHandler(json.payload);
          console.log('Response JSON:', json);
        };

        this['onClose'] = function () {
          let msg = 'Atmosphere connection was closed!';
          console.log(msg);
          notificationHandler(msg);
        };

        this['onError'] = function (response) {
          console.log('Response Error:', response);
          notificationHandler('Sorry, but there\'s some problem with your socket ' +
            'or the server is down!');
        };

        this['onTransportFailure'] = function (errorMsg, request) {
          console.log('Request Error:', request);
          notificationHandler('Sorry, but the selected transport method is not available! ' +
            'Falling back to the alternate one.');

          request.fallbackTransport = 'long-polling';
        };

        this['onReconnect'] = function (request) {
          let msg = 'Connection lost, trying to reconnect. ' +
            'Trying to reconnect in ' + request.reconnectInterval + 'ms.';

          console.log(msg);
          notificationHandler(msg);
        };

        this['onClientTimeout'] = function (request) {
          let msg = 'Client closed the connection after a timeout. ' +
            'Reconnecting in ' + request.reconnectInterval + 'ms.';

          console.log(msg);
          notificationHandler(msg);

          setTimeout(function () {
            subscription = socket.subscribe(request);
          }, request.reconnectInterval);
        };
      }

      let atmosphereRequest = null;
      let subscription = null;
      let socket = atmosphere;
      let self = this;

      self.notification = {
        user: 'desiderati',
        message: 'Testing message sending via Pure Javascript!',
      };

      self.initialize = function () {
        if (atmosphereRequest != null) {
          socket.unsubscribeUrl(atmosphereRequest.url);
          subscription = '';
        }

        atmosphereRequest = new AtmosphereRequest(
          apiUrl + 'atm/notification/' + self.notification.user,
          subscription,
          msg => self.printMessage(msg)
        );

        subscription = socket.subscribe(atmosphereRequest);

        $scope.$on('$destroy', function () {
          socket.unsubscribeUrl(atmosphereRequest.url);
          subscription = '';
        });
      };

      self.sendMessage = function () {
        console.log('Sending message \'' + self.notification.message + '\' to server!');
        if (atmosphereRequest == null) {
          self.printMessage('Atmosphere not initialized!');
          return;
        }

        subscription.push(JSON.stringify({
          payload: self.notification.message,
        }));
      };

      self.printMessage = function (message) {
        $('#notificationContainer').show();
        $('<li/>').html(message).appendTo('#notificationBoard');
      };
    }],
  );

})(app);
