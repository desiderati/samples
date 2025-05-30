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

  app.controller('TrackController', [
    '$scope',
    'CsrfService',
    'TrackService',
    'TrackNotificationService',
    function ($scope, CsrfService, TrackService, TrackNotificationService) {

      let self = this;
      self.track = {
        id: null,
        trackName: null,
        author: null,
        duration: null,
      };

      self.tracks = [];

      self.fetchAllTracks = function () {
        TrackService.fetchAllTracks().then(
          function (response) {
            self.tracks = response;
          }
        );
      };

      self.fetchTrackByName = function (trackName) {
        TrackService.fetchTrackByName(trackName).then(
          function (response) {
            self.tracks = response;
          }
        );
      };

      self.createTrack = function (track) {
        TrackService.createTrack(track).then(
          function () {
            self.fetchAllTracks();
            self.reset();
          },
          function (errResponse) {
            self.handleErrorResponse('Error while creating Track.', errResponse);
          },
        );
      };

      self.updateTrack = function (track, id) {
        TrackService.updateTrack(track, id).then(
          function () {
            self.fetchAllTracks();
            self.reset();
          },
          function (errResponse) {
            self.handleErrorResponse('Error while updating Track.', errResponse);
          },
        );
      };

      self.deleteTrack = function (id) {
        TrackService.deleteTrack(id).then(self.fetchAllTracks);
      };

      // Form Operations

      /** @namespace errResponse.data.validationMessages */
      self.handleErrorResponse = function (errMsg, errResponse) {
        if (typeof (errResponse.validationMessages) !== 'undefined') {
          self.addValidationErrorMessages(errResponse.validationMessages);
        } else {
          self.addErrorMessage(errResponse.message || errMsg);
        }
      };

      self.addErrorMessage = function (message) {
        $('#errorsContainer').show();
        $('<li/>').html(message.trim()).appendTo('#errors');
      };

      self.addValidationErrorMessages = function (messages) {
        $('#errorsContainer').show();
        for (let i = 0; i < messages.length; i++) {
          const msgParts = messages[i].trim().split(':');

          const input = msgParts[0];
          const inputParts = input.split('.');
          const inputName = inputParts[inputParts.length - 1]

          const message = msgParts[1];
          $('<li/>').wrapInner(
            $('<a/>')
              .attr('href', 'javascript: void(0);')
              .html(message)
              .on('click', function () {
                // Focus on the invalid field.
                $('input[name=\'' + inputName + '\']').focus();
              })
          ).appendTo('#errors');
        }
      };

      self.clearErrorMessages = function () {
        $('#errorsContainer').hide();
        $('#errors').html('');
      };

      self.submit = function () {
        self.clearErrorMessages();
        if (self.track.id === null) {
          console.log('Saving new Track...', self.track);
          self.createTrack(self.track);
        } else {
          self.updateTrack(self.track, self.track.id);
          console.log('Track updated with id: ', self.track.id);
        }
      };

      self.reset = function () {
        self.track = {
          id: null,
          trackName: null,
          author: null,
          duration: null,
        };
        self.clearErrorMessages();
        $scope.trackForm.$setPristine();
      };

      // Grid Operations

      self.filter = function () {
        if (self.filterByTrackName === null) {
          self.fetchAllTracks();
        } else {
          self.fetchTrackByName(self.filterByTrackName);
        }
      };

      self.edit = function (id) {
        console.log('Track to be edited: ', id);
        for (let i = 0; i < self.tracks.length; i++) {
          if (self.tracks[i].id === id) {
            self.track = angular.copy(self.tracks[i]);
            break;
          }
        }
      };

      self.remove = function (id) {
        console.log('Track to be deleted: ', id);
        if (self.track.id === id) {
          self.reset();
        }
        self.deleteTrack(id);
      };

      // Execute after startup.
      self.clearErrorMessages();
      self.fetchAllTracks();

      CsrfService.getCsrfToken().then(
        () => console.log('CSRF Token fetched with success!')
      )

      // Initialize the notification service.
      TrackNotificationService.initialize(msg => {
        if (msg.startsWith('Constraint Violations:')) {
          self.addValidationErrorMessages(msg.replace('Constraint Violations:', '').trim().split('\n'));
        } else {
          self.addErrorMessage(msg);
        }

        self.fetchAllTracks();
      });
    }]
  );

})(app);
