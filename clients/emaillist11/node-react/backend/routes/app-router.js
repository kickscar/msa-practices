const express = require('express');
const { error404, error500, jsonResult, oAuth2AuthorizationRequestRedirect, oAuth2LoginAuthentication } = require('../middlewares');
const { dispatcher } = require('./dispatcher');

exports.buildRouter = (app) => app
    .use(oAuth2AuthorizationRequestRedirect)
    .use(oAuth2LoginAuthentication)
    .use(jsonResult)
    .use('/', dispatcher)
    .use(error404)
    .use(error500);