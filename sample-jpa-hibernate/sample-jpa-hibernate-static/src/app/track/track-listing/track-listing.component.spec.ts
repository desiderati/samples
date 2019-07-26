/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TrackListingComponent} from './track-listing.component';

describe('TrackListingComponent', () => {
  let component: TrackListingComponent;
  let fixture: ComponentFixture<TrackListingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TrackListingComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrackListingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
