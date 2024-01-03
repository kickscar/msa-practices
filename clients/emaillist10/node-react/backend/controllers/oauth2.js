const configClient = require(`../config/oauth2${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);
const configJWT = require(`../config/jwt${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);

exports.refreshToken = async (req, res) => {
    try {
        const cookieRefreshToken = req.cookies[configJWT['refresh-token-cookie-name']];
        
        if (!cookieRefreshToken) {
           throw new Error('Refresh Token Not Exist in Cookie');
        }

        const clientRegistration = configClient.registration['emaillist-oauth2-client'];
        const response = await fetch(clientRegistration.provider['token-uri'], {
            method: 'post', 
            headers: {
                'Authorization': `Basic ${btoa(clientRegistration["client-id"] + ":" + clientRegistration["client-secret"])}`,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: (obj => {
                var qs = [];
                for(prop in obj) {
                    qs.push(prop + "=" + obj[prop]);
                }       
                return qs.join("&");
            })({
                grant_type: "refresh_token",
                refresh_token: cookieRefreshToken
            })
        });
        
        if(!response.ok) {
            next?.(new Error(`${response.status} ${response.statusText}`));
            return;
        }
    
        tokens = await response.json();

        res
            .cookie(configJWT['refresh-token-cookie-name'], tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: tokens['refresh_expires_in'] * 1000 }))
            .json(tokens['access_token']);
    
    } catch (err) {
        // 401 Unauthorized: Empty, Invalid, Expired
        res.status(401).json(err);
    }
};

exports.logout = async (req, res) => {
    try {
        const cookieRefreshToken = req.cookies[configJWT['refresh-token-cookie-name']];    
        const authHeader = req.headers['authorization'];
        const accessToken = authHeader?.split(' ')[1];

        const clientRegistration = configClient.registration['emaillist-oauth2-client'];
        const response = await fetch(clientRegistration.provider['logout-uri'], {
            method: 'post', 
            headers: Object.assign({}, {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            }, accessToken ? {'Authorization': `Bearer ${accessToken}`} : null),
            body: (obj => {
                var qs = [];
                for(prop in obj) {
                    qs.push(prop + "=" + obj[prop]);
                }       
                return qs.join("&");
            })({
                client_id: clientRegistration["client-id"],
                client_secret: clientRegistration["client-secret"],
                refresh_token: cookieRefreshToken
            })
        });
        
        // receive response(204 NO_CONTENT) 

        // response
        res
            .cookie(configJWT['refresh-token-cookie-name'], '', Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: 0 }))
            .json(null);

      } catch (err) {
        // 401 Unauthorized: Empty, Invalid, Expired
        res.status(401).json(err);
      }
};