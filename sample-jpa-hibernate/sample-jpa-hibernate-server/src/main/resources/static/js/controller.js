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
'use strict';

App.controller('TrackController', [
    '$scope',
    'TrackService',
    function ($scope, TrackService) {

        let self = this;

        self.track = {
            id: null,
            trackName: null,
            author: null,
            duration: null
        };

        self.tracks = [];

        self.fetchAllTracks = function () {
            TrackService.fetchAllTracks().then(
                function (response) {
                    self.tracks = response;
                },
                function () {
                    console.error('Error while fetching Tracks.');
                }
            );
        };

        self.fetchTrackByName = function (trackName) {
            TrackService.fetchTrackByName(trackName).then(
                function (response) {
                    self.tracks = response;
                },
                function () {
                    console.error('Error while fetching Tracks.');
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
                    self.handleErrorResponse(errResponse, 'Error while creating Track.');
                }
            );
        };

        self.updateTrack = function (track, id) {
            TrackService.updateTrack(track, id).then(
                function () {
                    self.fetchAllTracks();
                    self.reset();
                },
                function (errResponse) {
                    self.handleErrorResponse(errResponse, 'Error while updating Track.');
                }
            );
        };

        self.deleteTrack = function (id) {
            TrackService.deleteTrack(id).then(
                self.fetchAllTracks,
                function () {
                    console.error('Error while deleting Track.');
                }
            );
        };

        // Form Operations

        /** @namespace errResponse.data.validationMessages */
        self.handleErrorResponse = function (errResponse, errMsg) {
            console.error(errMsg);
            if (typeof (errResponse.data.validationMessages) !== 'undefined') {
                self.addValidationErrorMessages(errResponse.data.validationMessages);
            } else {
                self.addErrorMessage(errResponse.data.message);
            }
        };

        self.addErrorMessage = function (message) {
            $('#errorsContainer').show();
            $('<li/>').html(message).appendTo('#errors');
        };

        self.addValidationErrorMessages = function (messages) {
            $('#errorsContainer').show();
            for (let i = 0; i < messages.length; i++) {
                const input = messages[i].split(':')[0];
                const message = messages[i].split(':')[1];
                $('<li/>')
                    .wrapInner(
                        $('<a/>')
                            .attr('href', 'javascript: void(0);')
                            .html(message)
                            .on('click', function () {
                                // Focus on the invalid field
                                $("input[name='" + input + "']").focus();
                            })
                    )
                    .appendTo('#errors');
            }
        };

        self.clearErrorMessages = function () {
            $('#errorsContainer').hide();
            $('#errors').html('');
        };

        self.submit = function () {
            self.clearErrorMessages();
            if (self.track.id === null) {
                console.log('Saving new Track', self.track);
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
                duration: null
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

        // Execute After Start Up
        self.clearErrorMessages();
        self.fetchAllTracks();
    }]
);
