/*
 * Copyright (c) 2025 - Felipe Desiderati
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

import {Component, ViewChild} from '@angular/core';
import {FormsModule, NgForm} from '@angular/forms';

import {Notification} from './notification';
import {AtmosphereService} from './atmosphere.service';

import {environment} from '../environments/environment';

import $ from 'jquery';
import {CommonModule} from '@angular/common';

const notificationUrl = environment.notificationUrl;

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    imports: [
        FormsModule,
        CommonModule
    ],
    styleUrls: ['./app.component.css']
})
export class AppComponent {

    title = 'sample-atmosphere-static';
    notification = new Notification('desiderati', 'Testing message sending via Angular!');

    @ViewChild('atmosphereInitializationForm', {static: false})
    atmosphereInitializationForm!: NgForm;

    @ViewChild('atmosphereForm', {static: false})
    atmosphereForm!: NgForm;

    constructor(private atmosphereService: AtmosphereService) {
    }

    static printMessage(message: string) {
        $('#notificationContainer').show();
        $('<div/>', {class: 'alert alert-success'}).text(message).appendTo('#notificationBoard');
    }

    static printErrorMessage(message: string) {
        $('#notificationContainer').show();
        $('<div/>', {class: 'alert alert-warning'}).text(message).appendTo('#notificationBoard');
    }

    initialize() {
        this.atmosphereService.connect(
            notificationUrl + this.notification.user,
            {
                logLevel: 'debug',
                logFunction: AppComponent.printErrorMessage
            }
        ).subscribe({
            next: AppComponent.printMessage,
            error: AppComponent.printErrorMessage
        });
    }

    sendNotification() {
        console.log('Sending message \'' + this.notification.payload + '\' to server!');
        if (!this.atmosphereService.isInitialized()) {
            AppComponent.printErrorMessage('Notification Service not initialized!');
            return;
        }
        this.atmosphereService.sendNotification(this.notification);
    }
}
