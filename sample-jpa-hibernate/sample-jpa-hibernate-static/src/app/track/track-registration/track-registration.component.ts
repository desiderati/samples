/*
 * Copyright (c) 2024 - Felipe Desiderati
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
import {Component, EventEmitter, OnInit, Output, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {HttpErrorResponse} from '@angular/common/http';

import {Track} from '../track';
import {TrackService} from '../track.service';

import * as $ from 'jquery';

@Component({
    selector: 'app-track-registration',
    templateUrl: 'track-registration.component.html',
    styleUrls: ['./track-registration.component.css']
})
export class TrackRegistrationComponent implements OnInit {

    track = new Track(null, null, null, null);

    @ViewChild('trackRegistrationForm') trackRegistrationForm: NgForm;

    @Output() trackCreatedUpdatedDeletedEvent = new EventEmitter();

    constructor(private trackService: TrackService) {
    }

    static clearErrorMessages() {
        $('#errorsContainer').hide();
        $('#errors').html('');
    }

    static addErrorMessage(message: string) {
        $('#errorsContainer').show();
        $('<li/>').html(message).appendTo('#errors');
    }

    static addValidationErrorMessages(messages: string[]) {
        $('#errorsContainer').show();
        for (let i = 0; i < messages.length; i++) {
            const input = messages[i].split(':')[0];
            const message = messages[i].split(':')[1];
            $('<li/>')
                .wrapInner(
                    $('<a/>')
                        .attr('href', 'javascript: void(0);')
                        .html(message)
                        .on('click', function() {
                            // Focus on the invalid field
                            $('input[name=\'' + input + '\']').focus();
                        })
                )
                .appendTo('#errors');
        }
    }

    ngOnInit(): void {
        TrackRegistrationComponent.clearErrorMessages();
    }

    createTrack(track: Track) {
        this.trackService.createTrack(track).subscribe(
            () => {
                this.reset();
                this.trackCreatedUpdatedDeletedEvent.emit();
            },
            (responseError: HttpErrorResponse) => {
                if (typeof (responseError.error.validationMessages) !== 'undefined') {
                    TrackRegistrationComponent.addValidationErrorMessages(responseError.error.validationMessages);
                } else {
                    TrackRegistrationComponent.addErrorMessage(responseError.error.message);
                }
            });
    }

    configureTrack(track: Track) {
        this.track = track;
    }

    updateTrack(track: Track, id: number) {
        this.trackService.updateTrack(track, id).subscribe(
            () => {
                this.reset();
                this.trackCreatedUpdatedDeletedEvent.emit();
            },
            (responseError: HttpErrorResponse) => {
                if (typeof (responseError.error.validationMessages) !== 'undefined') {
                    TrackRegistrationComponent.addValidationErrorMessages(responseError.error.validationMessages);
                } else {
                    TrackRegistrationComponent.addErrorMessage(responseError.error.message);
                }
            });
    }

    deleteTrack(track: Track) {
        this.trackService.deleteTrack(track.id).subscribe(
            () => {
                if (this.track.id === track.id) {
                    this.reset();
                }
                this.trackCreatedUpdatedDeletedEvent.emit();
            });
    }

    submit() {
        TrackRegistrationComponent.clearErrorMessages();
        if (this.track.id === null) {
            console.log('Saving new track:', this.track);
            this.createTrack(this.track);
        } else {
            console.log('Updating track:', this.track);
            this.updateTrack(this.track, this.track.id);
        }
    }

    reset() {
        this.track = {
            id: null,
            trackName: null,
            author: null,
            duration: null
        };
        TrackRegistrationComponent.clearErrorMessages();
        this.trackRegistrationForm.reset();
    }
}
