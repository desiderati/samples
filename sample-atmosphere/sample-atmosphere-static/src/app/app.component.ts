/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {Component, OnDestroy, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';

import {Notification} from './notification';
import {NotificationService} from './notification.service';

import {environment} from '../environments/environment';

import * as $ from 'jquery';

const apiUrl = environment.apiUrl;

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {

    notification = new Notification('desiderati', 'Testing message sending via Angular!');

    private notificationSerive: NotificationService = new NotificationService(
        AppComponent.printMessage, AppComponent.printErrorMessage);

    @ViewChild('atmosphereInitializationForm', {static: false}) atmosphereInitializationForm: NgForm;
    @ViewChild('atmosphereForm', {static: false}) atmosphereForm: NgForm;

    constructor() {
    }

    static printMessage(message) {
        $('#notificationContainer').show();
        $('<div/>', {class : 'alert alert-success'}).text(message).appendTo('#notificationBoard');
    }

    static printErrorMessage(message) {
        $('#notificationContainer').show();
        $('<div/>', {class : 'alert alert-warning'}).text(message).appendTo('#notificationBoard');
    }

    initialize() {
        this.notificationSerive.subscribe(apiUrl + 'samples_notification/' + this.notification.user);
    }

    sendMessage() {
        console.log('Sending message \'' + this.notification.message + '\' to server!');
        if (!this.notificationSerive.isInitialized()) {
            AppComponent.printErrorMessage('Notification Service not initialized!');
            return;
        }
        this.notificationSerive.push(this.notification.message);
    }

    ngOnDestroy(): void {
        this.notificationSerive.unsubscribe();
    }
}
