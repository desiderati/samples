/*
 * Copyright (c) 2018 Sinax - Soluções Integradas ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
export class SimpleComponentController {
    textToShow: string;

    constructor() {
        this.textToShow = 'Carlos Rodrigues';
    }
}

export function SimpleComponentDirective() {
    return {
        restrict: 'E',
        scope: {},
        bindToController: {
            textToShow: '@'
        },
        template: require('./simple.component.html'),
        controller: SimpleComponentController,
        controllerAs: 'ctrl'
    };
}
