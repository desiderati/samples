'use strict';

App.controller('TrackController', [
    '$scope',
    'TrackService',
    function ($scope, TrackService) {

        let self = this;

        self.track = {
            id: null,
            trackname: null,
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
                });
        };

        self.fetchTrackByName = function (trackname) {
            TrackService.fetchTrackByName(trackname).then(
                function (response) {
                    self.tracks = response;
                },
                function () {
                    console.error('Error while fetching Tracks.');
                });
        };

        self.createTrack = function (track) {
            TrackService.createTrack(track).then(
                function () {
                    self.fetchAllTracks();
                    self.reset();
                },
                /** @namespace errResponse.data.validationMessages */
                function (errResponse) {
                    console.error('Error while creating Track.');
                    if (typeof(errResponse.data.validationMessages) !== 'undefined') {
                        self.addValidationErrorMessages(errResponse.data.validationMessages);
                    } else {
                        self.addErrorMessage(errResponse.data.message);
                    }
                });
        };

        self.updateTrack = function (track, id) {
            TrackService.updateTrack(track, id).then(
                function () {
                    self.fetchAllTracks();
                    self.reset();
                },
                /** @namespace errResponse.data.validationMessages */
                function (errResponse) {
                    console.error('Error while updating Track.');
                    if (typeof(errResponse.data.validationMessages) !== 'undefined') {
                        self.addValidationErrorMessages(errResponse.data.validationMessages);
                    } else {
                        self.addErrorMessage(errResponse.data.message);
                    }
                });
        };

        self.deleteTrack = function (id) {
            TrackService.deleteTrack(id).then(
                self.fetchAllTracks,
                function () {
                    console.error('Error while deleting Track.');
                });
        };

        // Form Operations

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
            }
            else {
                self.updateTrack(self.track, self.track.id);
                console.log('Track updated with id: ', self.track.id);
            }
        };

        self.reset = function () {
            self.track = {
                id: null,
                trackname: null,
                author: null,
                duration: null
            };
            self.clearErrorMessages();
            $scope.trackForm.$setPristine();
        };

        // Grid Operations

        self.filter = function () {
            if (self.filterByTrackname === null) {
                self.fetchAllTracks();
            }
            else {
                self.fetchTrackByName(self.filterByTrackname);
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
