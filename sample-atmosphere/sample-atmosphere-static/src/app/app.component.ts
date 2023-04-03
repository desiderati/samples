/*
 * Copyright (c) 2023 - Felipe Desiderati
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
import { Component, OnDestroy, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';

import { Notification } from './notification';
import { NotificationService } from './notification.service';

import { environment } from '../environments/environment';

import * as $ from 'jquery';

const apiUrl = environment.apiUrl;

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnDestroy {

    notification = new Notification('desiderati', 'Testing message sending via Angular!');

    private notificationService: NotificationService =
        new NotificationService(AppComponent.printMessage, AppComponent.printErrorMessage);

    @ViewChild('atmosphereInitializationForm', {static: false}) atmosphereInitializationForm: NgForm;
    @ViewChild('atmosphereForm', {static: false}) atmosphereForm: NgForm;

    constructor() {
    }

    static printMessage(message) {
        $('#notificationContainer').show();
        $('<div/>', {class: 'alert alert-success'}).text(message).appendTo('#notificationBoard');
    }

    static printErrorMessage(message) {
        $('#notificationContainer').show();
        $('<div/>', {class: 'alert alert-warning'}).text(message).appendTo('#notificationBoard');
    }

    initialize() {
        this.notificationService.subscribe(apiUrl + 'samples_notification/' + this.notification.user);
    }

    sendMessage() {
        console.log('Sending message \'' + this.notification.message + '\' to server!');
        if (!this.notificationService.isInitialized()) {
            AppComponent.printErrorMessage('Notification Service not initialized!');
            return;
        }
        this.notificationService.push(this.notification.message);
    }

    ngOnDestroy(): void {
        this.notificationService.unsubscribe();
    }
}
