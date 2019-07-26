/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl} from '@angular/forms';

import {Track} from '../track';
import {TrackService} from '../track.service';

@Component({
  selector: 'app-track-listing',
  templateUrl: 'track-listing.component.html',
  styleUrls: ['./track-listing.component.css']
})
export class TrackListingComponent implements OnInit {

  @Output() trackUpdateEvent = new EventEmitter<Track>();
  @Output() trackRemoveEvent = new EventEmitter<Track>();

  tracks: Track[];

  filterByTracknameInput = new FormControl('');

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

  fetchTrackByName(trackname: string) {
    this.trackService.fetchTrackByName(trackname).subscribe(
      (response: Track[]) => {
        this.tracks = response;
      });
  }

  filter() {
    if (this.filterByTracknameInput.value === null) {
      this.fetchAllTracks();
    } else {
      this.fetchTrackByName(this.filterByTracknameInput.value);
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
