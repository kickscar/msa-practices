const path = require('path')
const nextConfig = {
    sassOptions: {
        includePaths: [path.join(__dirname, 'src', 'assets', 'scss')]
    },
    async rewrites() {
        return [{
            source: "/:any*",
            destination: "/"
        }];
    },
    env: {
        API_BASE_ENDPOINT: 'http://localhost:8888/api/emaillist',
        REFRESH_TOKEN_ENDPOINT: '/refresh-token',
        AUTHORIZATION_ENDPOINT: '/oauth2/authorize/emaillist-oauth2-client'
    }    
};
  
module.exports = nextConfig;