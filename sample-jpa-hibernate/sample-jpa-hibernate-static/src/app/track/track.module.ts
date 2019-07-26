/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {TrackRegistrationComponent} from './track-registration/track-registration.component';
import {TrackListingComponent} from './track-listing/track-listing.component';
import {TrackComponent} from './track.component';

@NgModule({
  declarations: [
    TrackComponent,
    TrackRegistrationComponent,
    TrackListingComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: [
    TrackComponent,
    TrackRegistrationComponent,
    TrackListingComponent]
})
export class TrackModule {
}
