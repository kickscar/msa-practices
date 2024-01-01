const path = require('path')
const nextConfig = {
    sassOptions: {
        includePaths: [path.join(__dirname, 'src', 'assets', 'scss')]
    },
    async rewrites() {
        return [{
            source: "/:any*",
            destination: "/",
        }];
    }    
};
  
module.exports = nextConfig;