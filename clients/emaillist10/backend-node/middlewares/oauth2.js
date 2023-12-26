
const configClient = require('../config/oauth2.json');

console.log(configClient);

exports.oAuth2AuthorizationRequestRedirect = (req, res, next) => {
    const urlPath = req.originalUrl;
    const authorizationRequestUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].authorization}/)(.*)`);

    if(!authorizationRequestUriPattern.test(urlPath)) {
        next?.();
        return;
    }

    const registrationId = urlPath.replace(authorizationRequestUriPattern, "$2");
    const clientRegistration = configClient.registration[registrationId];
    if(!clientRegistration) {
        next?.(new Error("Client Registration Not Found"));
        return;
    }

    const authorizationRequest = {
        response_type: "code",
        client_id: clientRegistration["client-id"],
        scope: clientRegistration["scope"].join(" "),
        edirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
    }
    // const authorizationRequestUri = `${clientRegistration.provider["authorization-uri"]}?${}`;

    console.log(clientRegistration);
    res.end();
}