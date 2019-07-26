/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {TestBed} from '@angular/core/testing';

import {TrackService} from './track.service';

describe('TrackService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TrackService = TestBed.get(TrackService);
    expect(service).toBeTruthy();
  });
});
