/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
import {Notification} from './notification';

describe('Notification', () => {
    it('should create an instance', () => {
        expect(new Notification(null, null)).toBeTruthy();
    });
});
