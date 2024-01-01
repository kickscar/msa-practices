import { NextResponse } from 'next/server'

const configClient = JSON.parse(process.env.OAUTH2_CLIENT_CONFIG);
const configJWT = JSON.parse(process.env.JWT_CONFIG);

export const middleware = async (request) => {
  try {
    const requestUri = request.nextUrl.pathname;

    //
    const authorizationRequestUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].authorization}/)(.*)`);
    
    if (authorizationRequestUriPattern.test(requestUri)) {
      const registrationId = requestUri.replace(authorizationRequestUriPattern, "$2");
      return oAuth2AuthorizationRequestRedirect(registrationId);
    }

    //    
    const authenticationRedirectUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].redirection}/)(.*)`);
    
    if (authenticationRedirectUriPattern.test(requestUri)) {
      const registrationId = requestUri.replace(authenticationRedirectUriPattern, "$2");
      const authorizationCode = request.nextUrl.searchParams.get('code');

      const tokens = await oAuth2LoginAuthentication(registrationId, authorizationCode);
      // console.log(tokens);

      const redirectUrl = request.nextUrl.clone();
      redirectUrl.pathname = "/";
      redirectUrl.search = "";

      const response = NextResponse.redirect(redirectUrl);
      response.cookies.set(configJWT['refresh-token-cookie-name'], tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: tokens['refresh_expires_in'] * 1000 }));
      
      return response;
    }

  } catch (error) {
    console.error(error);
  }

  return NextResponse.next();
}




//
// oauth2 functions: authorization code grant type flow 
//


/**
 * 
 * @param {*} registrationId 
 * @returns 
 */
const oAuth2AuthorizationRequestRedirect = (registrationId) => {
  const clientRegistration = configClient.registration[registrationId];

  if (!clientRegistration) {
    throw new Error(`Client Registration[${registrationId}] Not Found`);
  }

  const authorizationRequestUri = new URL(clientRegistration.provider["authorization-uri"]);
  authorizationRequestUri.search = (obj => {
    const qs = [];
    for (let prop in obj) {
      qs.push(prop + "=" + obj[prop]);
    }
    return qs.join("&");
  })({
    response_type: "code",
    client_id: clientRegistration["client-id"],
    scope: encodeURIComponent(clientRegistration["scope"].join(" ")),
    redirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
  });

  return NextResponse.redirect(authorizationRequestUri);
}

/**
 * 
 * @param {*} registrationId 
 * @returns 
 */
const oAuth2LoginAuthentication = async (registrationId, authorizationCode) => {
  const clientRegistration = configClient.registration[registrationId];

  if (!clientRegistration) {
    throw new Error(`Client Registration[${registrationId}] Not Found`);
  }

  const response = await fetch(clientRegistration.provider['token-uri'], {
    method: 'post',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: (obj => {
      const qs = [];
      for (let prop in obj) {
        qs.push(prop + "=" + obj[prop]);
      }
      return qs.join("&");
    })({
      grant_type: clientRegistration["authorization-grant-type"],
      client_id: clientRegistration["client-id"],
      client_secret: clientRegistration["client-secret"],
      code: authorizationCode,
      client_secret: clientRegistration["client-secret"],
      redirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
    })
  });

  if (!response.ok) {
    throw new Error(`${response.status} ${response.statusText}`);
  }

  return await response.json();
}

