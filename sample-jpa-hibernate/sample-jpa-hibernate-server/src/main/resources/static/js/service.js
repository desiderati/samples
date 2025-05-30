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

  const apiUrl = 'http://localhost:9090/sample-jpa-hibernate/'

  app.factory('CsrfService', ['$http', '$q', function ($http, $q) {
    return {
      /**
       * When logging in for the first time using form-based authentication,
       * the CSRF cookie is not set upon redirection to the main page.
       * This happens because Spring Security hasn’t had the chance to include
       * the CSRF token in a response body (HTML or JSON).
       * The token is only sent in responses with a body—not in 302 Found redirects.
       * By doing this, we force the CSRF token to be set right after login.
       */
      getCsrfToken: function () {
        return $http.get(apiUrl + 'csrf').then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while fetching CSRF Token.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      }
    }
  }]);

  app.factory('TrackService', ['$http', '$q', function ($http, $q) {
    return {
      fetchAllTracks: function () {
        return $http.get(apiUrl + 'api/v1/track').then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while fetching Tracks.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      },

      fetchTrackByName: function (trackName) {
        if (!!trackName) {
          return $http.get(apiUrl + 'api/v1/track/' + trackName).then(
            function (response) {
              return response.data;
            },
            function (errResponse) {
              console.error('Error while fetching Tracks.', errResponse);
              return $q.reject(errResponse.data);
            },
          );
        } else {
          return this.fetchAllTracks();
        }
      },

      createTrack: function (track) {
        return $http.post(apiUrl + 'api/v1/track', track).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while creating Track.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      },

      updateTrack: function (track, id) {
        return $http.put(apiUrl + 'api/v1/track/' + id, track).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while updating Track.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      },

      deleteTrack: function (id) {
        return $http.delete(apiUrl + 'api/v1/track/' + id).then(
          function (response) {
            return response.data;
          },
          function (errResponse) {
            console.error('Error while deleting Track.', errResponse);
            return $q.reject(errResponse.data);
          },
        );
      },
    };
  }]);

  app.factory('TrackNotificationService', ['$rootScope', function ($rootScope) {
    // Link: https://github.com/Atmosphere/atmosphere/wiki/atmosphere.js-API
    function AtmosphereRequest(url, notificationHandler) {

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
          socket.subscribe(request);
        }, request.reconnectInterval);
      };
    }

    let atmosphereRequest = null;
    let socket = atmosphere;
    let self = this;

    self.notification = {
      user: 'jpa-hibernate'
    };

    self.initialize = function (onMessageReceive) {
      if (atmosphereRequest != null) {
        socket.unsubscribeUrl(atmosphereRequest.url);
      }

      atmosphereRequest = new AtmosphereRequest(
        apiUrl + 'atm/notification/' + self.notification.user,
        onMessageReceive
      );

      socket.subscribe(atmosphereRequest);

      $rootScope.$on('$destroy', function () {
        socket.unsubscribeUrl(atmosphereRequest.url);
      });
    };

    return self;
  }]);

})(app);
