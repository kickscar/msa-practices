const express = require('express');
const { error404, error500, jsonResult, acceptOnlyJsonRequest, oAuth2AuthorizationRequestRedirect, oAuth2LoginAuthentication } = require('../middlewares');
const { dispatcher } = require('./dispatcher');
// const { dispatcherApi } = require('./dispatcher-api');

exports.appRouter = (app) => app
    .use(oAuth2AuthorizationRequestRedirect)
    .use(oAuth2LoginAuthentication)
    .use(jsonResult)
    .use('/', dispatcher)
//    .use('/api', acceptOnlyJsonRequest, dispatcherApi)
    .use(error404)
    .use(error500);