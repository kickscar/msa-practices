import { NextResponse } from 'next/server'

const configClient = JSON.parse(process.env.OAUTH2_CLIENT_CONFIG);
const configJWT = JSON.parse(process.env.JWT_CONFIG);

export const middleware = async (request) => {
  try {
    const requestUri = request.nextUrl.pathname;

    // request authorization
    const authorizationRequestUriPattern = new RegExp(`(${configClient['endpoint-baseUri'].authorization}/)(.*)`);

    if (authorizationRequestUriPattern.test(requestUri)) {
      const registrationId = requestUri.replace(authorizationRequestUriPattern, "$2");
      return oAuth2AuthorizationRequestRedirect(registrationId);
    }

    // requests tokens   
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

    // refresh token
    if (requestUri === configClient['endpoint-baseUri'].refreshToken) {
      const cookieRefreshToken = request.cookies.get(configJWT['refresh-token-cookie-name']);
      if (!cookieRefreshToken) {
        return NextResponse.json({result: 'fail', data: null, message: 'Refresh Token Not Exist in Cookie'}, {status: 401});
      }

      const clientRegistration = configClient.registration['emaillist-oauth2-client'];
      const clientId = clientRegistration["client-id"];
      const clientSecret = clientRegistration["client-secret"];
      const tokenUri = clientRegistration.provider['token-uri'];
      const tokens = await oAuth2RefreshToken(tokenUri, clientId, clientSecret, cookieRefreshToken.value);

      const response = NextResponse.json({result: 'success', data: tokens['access_token'], message: null}, {status: 200});
      response.cookies.set(configJWT['refresh-token-cookie-name'], tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: tokens['refresh_expires_in'] * 1000 }));

      return response;
    }

    // logout
    if (requestUri === configClient['endpoint-baseUri'].logout) {
      const cookieRefreshToken = request.cookies.get(configJWT['refresh-token-cookie-name']);
      const authHeader = request.headers.get('authorization');
      const accessToken = authHeader?.split(' ')[1];
      
      const clientRegistration = configClient.registration['emaillist-oauth2-client'];
      const clientId = clientRegistration["client-id"];
      const clientSecret = clientRegistration["client-secret"];
      const logoutUri = clientRegistration.provider['logout-uri'];
      
      await oAuth2Logout(logoutUri, clientId, clientSecret, accessToken, cookieRefreshToken.value);
      
      const response = NextResponse.json({result: 'success', data: null, message: null}, {status: 200});
      response.cookies.delete(configJWT['refresh-token-cookie-name']);
      // response.cookies.set(configJWT['refresh-token-cookie-name'], tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: 0 }));

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
  authorizationRequestUri.search = new URLSearchParams({
    response_type: "code",
    client_id: clientRegistration["client-id"],
    scope: clientRegistration["scope"].join(" "),
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
    body: new URLSearchParams({
      grant_type: clientRegistration["authorization-grant-type"],
      client_id: clientRegistration["client-id"],
      client_secret: clientRegistration["client-secret"],
      code: authorizationCode,
      redirect_uri: clientRegistration["redirect-uri"].replace("{registrationId}", registrationId)
    })
  });

  if (!response.ok) {
    throw new Error(`${response.status} ${response.statusText}`);
  }

  return await response.json();
}

/**
 * 
 * @param {*} tokenUri 
 * @param {*} clientId 
 * @param {*} clientSecret 
 * @param {*} refreshToken 
 * @returns 
 */
const oAuth2RefreshToken = async (tokenUri, clientId, clientSecret, refreshToken) => {
  const response = await fetch(tokenUri, {
    method: 'post',
    headers: {
      'Authorization': `Basic ${btoa(clientId + ":" + clientSecret)}`,
      'Accept': 'application/json',
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    body: new URLSearchParams({
      grant_type: "refresh_token",
      refresh_token: refreshToken
    })
  });

  if (!response.ok) {
    throw new Error(`${response.status} ${response.statusText}`);
  }

  return await response.json();
}


const oAuth2Logout = async (logoutUri, clientId, clientSecret, accessToken, refreshToken) => {
  const response = await fetch(logoutUri, {
    method: 'post', 
    headers: Object.assign({}, {
        'Accept': 'application/json',
        'Content-Type': 'application/x-www-form-urlencoded'
    }, accessToken ? {'Authorization': `Bearer ${accessToken}`} : null),
    body: new URLSearchParams({
        client_id: clientId,
        client_secret: clientSecret,
        refresh_token: refreshToken
    })
  });

  // receive response(204 NO_CONTENT)
}
  

