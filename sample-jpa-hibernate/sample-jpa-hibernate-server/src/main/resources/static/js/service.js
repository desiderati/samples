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
