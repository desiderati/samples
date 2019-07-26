/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
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
            .on('click', function () {
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
      trackname: null,
      author: null,
      duration: null
    };
    TrackRegistrationComponent.clearErrorMessages();
    this.trackRegistrationForm.reset();
  }
}
