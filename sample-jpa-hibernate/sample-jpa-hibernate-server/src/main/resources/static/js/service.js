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
'use strict';

App.factory('TrackService', ['$http', '$q', function ($http, $q) {
    return {
        fetchAllTracks: function () {
            return $http.get(document.location.toString() + 'api/v1/track/').then(
                function (response) {
                    return response.data;
                },
                function (errResponse) {
                    console.error('Error while fetching Tracks.');
                    return $q.reject(errResponse);
                });
        },

        fetchTrackByName: function (trackname) {
            return $http.get(document.location.toString() + 'api/v1/track/' + trackname).then(
                function (response) {
                    return response.data;
                },
                function (errResponse) {
                    console.error('Error while fetching Tracks.');
                    return $q.reject(errResponse);
                });
        },

        createTrack: function (track) {
            return $http.post(document.location.toString() + 'api/v1/track/', track).then(
                function (response) {
                    return response.data;
                },
                function (errResponse) {
                    console.error('Error while creating Track.');
                    return $q.reject(errResponse);
                });
        },

        updateTrack: function (track, id) {
            return $http.put(document.location.toString() + 'api/v1/track/' + id, track).then(
                function (response) {
                    return response.data;
                },
                function (errResponse) {
                    console.error('Error while updating Track.');
                    return $q.reject(errResponse);
                });
        },

        deleteTrack: function (id) {
            return $http.delete(document.location.toString() + 'api/v1/track/' + id).then(
                function (response) {
                    return response.data;
                },
                function (errResponse) {
                    console.error('Error while deleting Track.');
                    return $q.reject(errResponse);
                });
        }
    };
}]);
