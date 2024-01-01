const path = require('path')
const nextConfig = {
    sassOptions: {
        includePaths: [path.join(__dirname, 'src', 'assets', 'scss')]
    }
};
  
module.exports = nextConfig;