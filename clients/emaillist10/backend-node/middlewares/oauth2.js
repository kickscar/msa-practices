
const configClient = require(`../config/oauth2${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);
const configJWT = require(`../config/jwt${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);

exports.oAuth2AuthorizationRequestRedirect = (req, res, next) => {
    const authorizationRequestUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].authorization}/)(.*)`);

    if(!authorizationRequestUriPattern.test(req.path)) {
        next?.();
        return;
    }

    const registrationId = req.path.replace(authorizationRequestUriPattern, "$2");
    const clientRegistration = configClient.registration[registrationId];
    if(!clientRegistration) {
        next?.(new Error("Client Registration Not Found"));
        return;
    }

    const authorizationRequestUri = `${clientRegistration.provider["authorization-uri"]}?${new URLSearchParams({
        response_type: "code",
        client_id: clientRegistration["client-id"],
        scope: clientRegistration["scope"].join(" "),
        redirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
    })}`;

    res.redirect(authorizationRequestUri);
}

exports.oAuth2LoginAuthentication = async (req, res, next) => {
    const authenticationRedirectUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].redirection}/)(.*)`);
 
    if(!authenticationRedirectUriPattern.test(req.path)) {
        next?.();
        return;
    }

    const registrationId = req.path.replace(authenticationRedirectUriPattern, "$2");
    const clientRegistration = configClient.registration[registrationId];

    const response = await fetch(clientRegistration.provider['token-uri'], {
        method: 'post', 
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            grant_type: clientRegistration["authorization-grant-type"],
            client_id: clientRegistration["client-id"],
            client_secret: clientRegistration["client-secret"],
            code: req.query['code'],
            client_secret: clientRegistration["client-secret"],
            redirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
        })
    });
    
    if(!response.ok) {
        next?.(new Error(`${response.status} ${response.statusText}`));
        return;
    }

    const tokens = await response.json();
    res
        .cookie(configJWT['refresh-token-cookie-name'], tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: tokens['refresh_expires_in'] * 1000 }))
        .redirect('/');
}