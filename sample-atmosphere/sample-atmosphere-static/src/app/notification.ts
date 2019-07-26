/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
export class Notification {

    user: string;
    message: string;

    constructor(user: string, message: string) {
        this.user = user;
        this.message = message;
    }
}
