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
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl } from '@angular/forms';

import { Track } from '../track';
import { TrackService } from '../track.service';

@Component({
    selector: 'app-track-listing',
    templateUrl: 'track-listing.component.html',
    styleUrls: ['./track-listing.component.css']
})
export class TrackListingComponent implements OnInit {

    @Output() trackUpdateEvent = new EventEmitter<Track>();
    @Output() trackRemoveEvent = new EventEmitter<Track>();

    tracks: Track[];

    filterByTrackNameInput = new FormControl('');

    constructor(private trackService: TrackService) {
    }

    ngOnInit(): void {
        this.fetchAllTracks();
    }

    fetchAllTracks() {
        this.trackService.fetchAllTracks().subscribe(
            (response: Track[]) => {
                this.tracks = response;
            });
    }

    fetchTrackByName(trackName: string) {
        this.trackService.fetchTrackByName(trackName).subscribe(
            (response: Track[]) => {
                this.tracks = response;
            });
    }

    filter() {
        if (this.filterByTrackNameInput.value === null) {
            this.fetchAllTracks();
        } else {
            this.fetchTrackByName(this.filterByTrackNameInput.value);
        }
    }

    update(id: number) {
        this.selectTrack(id, track => {
            this.trackUpdateEvent.emit(track);
            console.log('Track to be updated:', track);
        });
    }

    remove(id: number) {
        this.selectTrack(id, track => {
            this.trackRemoveEvent.emit(track);
            console.log('Track to be deleted:', track);
        });
    }

    private selectTrack(id: number, callback: any) {
        for (let i = 0; i < this.tracks.length; i++) {
            if (this.tracks[i].id === id) {
                const innerTrack = Object.assign({}, this.tracks[i]);
                callback(innerTrack);
                break;
            }
        }
    }
}
