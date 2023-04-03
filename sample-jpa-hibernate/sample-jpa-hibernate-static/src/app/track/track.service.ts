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
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Track } from './track';

import { environment } from '../../environments/environment';

const apiUrl = environment.apiUrl;

@Injectable({
    providedIn: 'root',
})
export class TrackService {

    private apiUrl = apiUrl + 'track';

    constructor(private http: HttpClient) {
    }

    fetchAllTracks() {
        return this.http.get(this.apiUrl).pipe(
            catchError(this.handleError('fetching all tracks')));
    }

    fetchTrackByName(trackName: string) {
        if (!!trackName) {
            return this.http.get(this.apiUrl + '/' + trackName).pipe(
                catchError(this.handleError('fetching track by name')));
        } else {
            this.fetchAllTracks();
        }
    }

    createTrack(track: Track) {
        return this.http.post(this.apiUrl, track).pipe(
            catchError(this.handleError('creating track')));
    }

    updateTrack(track: Track, id: number) {
        return this.http.put(this.apiUrl + '/' + id, track).pipe(
            catchError(this.handleError('updating track')));
    }

    deleteTrack(id: number) {
        return this.http.delete(this.apiUrl + '/' + id).pipe(
            catchError(this.handleError('deleting track')));
    }

    private handleError(errorMessage: String) {
        return (errorResponse: HttpErrorResponse) => {
            console.error('TrackService::Error while ' + errorMessage + '!', errorResponse.error);
            return throwError(errorResponse);
        };
    }
}
