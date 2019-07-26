/*
 * Copyright (c) 2019 - Felipe Desiderati ALL RIGHTS RESERVED.
 *
 * This software is protected by international copyright laws and cannot be
 * used, copied, stored or distributed without prior authorization.
 */
let WebpackMerge = require('webpack-merge');
let CommonConfig = require('./webpack.config.common.js');

module.exports = WebpackMerge(CommonConfig, {
    mode: "development"
});
