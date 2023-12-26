const configClient = require(`../config/oauth2${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);
const configJWT = require(`../config/jwt${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.json`);

exports.refreshToken = async (req, res) => {
    try {
        const cookieRefreshToken = req.cookies['refreshToken'];
    
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
        console.log(`token refreshed: ${JSON.stringify(tokens)}`);

        res
            .cookie('refreshToken', tokens['refresh_token'], Object.assign({}, configJWT['refresh-token-cookie-options'], { maxAge: tokens['refresh_expires_in'] }))
            .json(tokens['access_token']);
    
    } catch (err) {
        // 401 Unauthorized: Empty, Invalid, Expired
        res.status(401).json(err);
    }
};

exports.logout = async (req, res) => {
    res.json({});
};
