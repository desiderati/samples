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

App.factory('TrackService', ['$http', '$q', function ($http, $q) {

    let apiUrl = document.location.toString() + 'api/v1/track/';

    // Lembrar que estamos usando a versão 1.5 do Angular e que o componente $http ainda lida com Promises.
    return {
        fetchAllTracks: function () {
            let fetchAllTracksApiUrl = document.location.toString() + 'api/v1/track/';
            return null;
        },

        fetchTrackByName: function (trackName) {
            let fetchTrackByNameApiUrl = document.location.toString() + 'api/v1/track/' + trackName;
            return null;
        },

        createTrack: function (track) {
            let createTrackApiUrl = document.location.toString() + 'api/v1/track/';
            return null;
        },

        updateTrack: function (track, id) {
            let updateTrackApiUrl = document.location.toString() + 'api/v1/track/' + id;
            return null;
        },

        deleteTrack: function (id) {
            let deleteTrackApiUrl = document.location.toString() + 'api/v1/track/' + id;
            return null;
        }
    };
}]);
