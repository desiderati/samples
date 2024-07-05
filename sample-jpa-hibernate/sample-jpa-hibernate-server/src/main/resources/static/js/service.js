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
(function (app) {
  'use strict';

  app.factory('TrackService', ['$http', '$q', function ($http, $q) {
    return {
      fetchAllTracks: function () {
        return $http.get(document.location.toString() + 'api/v1/track').then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while fetching Tracks.');
            return $q.reject(errResponse);
          },
        );
      },

      fetchTrackByName: function (trackName) {
        if (!!trackName) {
          return $http.get(document.location.toString() + 'api/v1/track/' + trackName).then(
            function (response) {
              return response.data;
            },
            function (errResponse) {
              console.error('Error while fetching Tracks.');
              return $q.reject(errResponse);
            },
          );
        } else {
          return this.fetchAllTracks();
        }
      },

      createTrack: function (track) {
        return $http.post(document.location.toString() + 'api/v1/track', track).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while creating Track.');
            return $q.reject(errResponse);
          },
        );
      },

      updateTrack: function (track, id) {
        return $http.put(document.location.toString() + 'api/v1/track/' + id, track).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while updating Track.');
            return $q.reject(errResponse);
          },
        );
      },

      deleteTrack: function (id) {
        return $http.delete(document.location.toString() + 'api/v1/track/' + id).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while deleting Track.');
            return $q.reject(errResponse);
          },
        );
      },
    };
  }]);

  app.factory('TrackNotificationService', ['$rootScope', function ($rootScope) {
    // Link: https://github.com/Atmosphere/atmosphere/wiki/atmosphere.js-API
    function AtmosphereRequest(url, subscription, notificationHandler, notificationConsole) {

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
        notificationConsole(msg);

        // Carry the UUID. This is required if you want to call
        // subscribe(request) again.
        this.uuid = response.request.uuid;
      };

      this['onReopen'] = function (response) {
        let msg = 'Atmosphere reconnected using ' + response.transport + '!';
        console.log(msg);
        notificationConsole(msg);
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

        console.log('Response JSON:', json);
        notificationHandler(json.payload);
      };

      this['onClose'] = function () {
        let msg = 'Atmosphere connection was closed!';
        console.log(msg);
        notificationConsole(msg);
      };

      this['onError'] = function (response) {
        console.log('Response Error:', response);
        notificationConsole('Sorry, but there\'s some problem with your socket ' +
          'or the server is down!');
      };

      this['onTransportFailure'] = function (errorMsg, request) {
        console.log('Request Error:', request);
        notificationConsole('Sorry, but the selected transport method is not available! ' +
          'Falling back to the alternate one.');

        request.fallbackTransport = 'long-polling';
      };

      this['onReconnect'] = function (request) {
        let msg = 'Connection lost, trying to reconnect. ' +
          'Trying to reconnect in ' + request.reconnectInterval + 'ms.';

        console.log(msg);
        notificationConsole(msg);
      };

      this['onClientTimeout'] = function (request) {
        let msg = 'Client closed the connection after a timeout. ' +
          'Reconnecting in ' + request.reconnectInterval + 'ms.';

        console.log(msg);
        notificationConsole(msg);

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
      user: 'jpa-hibernate'
    };

    self.initialize = function (onMessageReceive) {
      if (atmosphereRequest != null) {
        socket.unsubscribeUrl(atmosphereRequest.url);
        subscription = '';
      }

      atmosphereRequest = new AtmosphereRequest(
        document.location.toString() + 'notification/' + self.notification.user,
        subscription,
        onMessageReceive,
        onMessageReceive
      );

      subscription = socket.subscribe(atmosphereRequest);

      $rootScope.$on('$destroy', function () {
        socket.unsubscribeUrl(atmosphereRequest.url);
        subscription = '';
      });
    };

    return self;
  }]);

})(app);
