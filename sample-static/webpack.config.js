/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
switch (process.env.NODE_ENV) {

    case 'prod':
    case 'production':
        module.exports = require('./webpack.config.prod');
        break;

    case 'qa':
    case 'quality-assurance':
        module.exports = require('./webpack.config.qa');
        break;

    case 'dev':
    case 'development':
    default:
        module.exports = require('./webpack.config.dev');
}
