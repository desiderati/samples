/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
export class Track {

  id: number;
  trackname: string;
  author: string;
  duration: string;

  constructor(id: number, trackname: string, author: string, duration: string) {
    this.id = id;
    this.trackname = trackname;
    this.author = author;
    this.duration = duration;
  }
}
