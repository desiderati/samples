/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {TrackModule} from './track.module';

describe('TrackModule', () => {
  let trackModule: TrackModule;

  beforeEach(() => {
    trackModule = new TrackModule();
  });

  it('should create an instance', () => {
    expect(trackModule).toBeTruthy();
  });
});
