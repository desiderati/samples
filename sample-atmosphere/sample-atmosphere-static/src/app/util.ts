/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
export class Util {

    static isJson(obj) {
        if (!obj || !Util.isObject(obj)) {
            return false;
        }

        try {
            const json = JSON.parse(JSON.stringify(obj));
            console.log(json);
            return Util.isObject(json);
        } catch (e) {
            return false;
        }
    }

    static isObject(obj) {
        return Object.prototype.toString.call(obj) === '[object Object]';
    }
}
