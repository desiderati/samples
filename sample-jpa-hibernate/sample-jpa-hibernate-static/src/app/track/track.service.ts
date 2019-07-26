/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';

import {throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

import {Track} from './track';

import {environment} from '../../environments/environment';

const apiUrl = environment.apiUrl;

@Injectable({
  providedIn: 'root'
})
export class TrackService {

  private apiUrl = apiUrl + 'track/';

  constructor(private http: HttpClient) {
  }

  fetchAllTracks() {
    return this.http.get(this.apiUrl).pipe(
      catchError(this.handleError('fetching all tracks')));
  }

  fetchTrackByName(trackname: string) {
    return this.http.get(this.apiUrl + trackname).pipe(
      catchError(this.handleError('fetching track by name')));
  }

  createTrack(track: Track) {
    return this.http.post(this.apiUrl, track).pipe(
      catchError(this.handleError('creating track')));
  }

  updateTrack(track: Track, id: number) {
    return this.http.put(this.apiUrl + id, track).pipe(
      catchError(this.handleError('updating track')));
  }

  deleteTrack(id: number) {
    return this.http.delete(this.apiUrl + id).pipe(
      catchError(this.handleError('deleting track')));
  }

  private handleError(errorMessage: String) {
    return (errorResponse: HttpErrorResponse) => {
      console.error('TrackService::Error while ' + errorMessage + '!', errorResponse.error);
      return throwError(errorResponse);
    };
  }
}
