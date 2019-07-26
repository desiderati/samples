/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
(function (angular) {
    'use strict';

    let app = angular.module('MyApp', []);
    app.controller('TestCtrl', ['$scope', function ($scope) {
        $scope.textToShow = "Felipe Desiderati";
    }]);

})(angular);
