/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */

///<reference path="../../references.d.ts"/>

import {SimpleComponentDirective} from "./components/simple.component";

angular.module('MyApp')
    .directive('simpleComponent', SimpleComponentDirective);
