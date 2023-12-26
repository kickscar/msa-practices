const { oAuth2AuthorizationRequestRedirect } = require('./oauth2');
const { acceptOnlyJsonRequest, jsonResult} = require('./json');
const { error404, error500 } = require('./error');

module.exports = { acceptOnlyJsonRequest, jsonResult, error404, error500, oAuth2AuthorizationRequestRedirect }; 
