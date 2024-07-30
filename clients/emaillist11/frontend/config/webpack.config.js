const path = require('path');
const webpack = require('webpack');
const CaseSensitivePathsPlugin = require('case-sensitive-paths-webpack-plugin');

module.exports = function(env) {
    return {
        mode: "none",
        entry: path.resolve(`src/index.js`),
        output: {
            path: path.resolve('../backend/src/main/resources'),
            filename: 'assets/js/main.js',
            assetModuleFilename: 'assets/images/[hash][ext]'
        },
        module: {
            rules:[{
                test: /\.js/i,
                exclude: /node_modules/,
                loader: 'babel-loader',
                options: {
                    configFile: path.resolve('config/babel.config.json')
                }
            }, {
                test: /\.(c|sa|sc)ss$/i,
                use:[
                    'style-loader', 
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true
                        }
                    }, 
                    'sass-loader'
                ]
            }, {
                test: /\.(png|gif|jp?eg|svg|ico|tif?f|bmp)/i,
                type: 'asset/resource'
            }]
        },
        plugins: [
            new CaseSensitivePathsPlugin(),
            new webpack.DefinePlugin({
                API_BASE_ENDPOINT: JSON.stringify(process.env.NODE_ENV === 'development' ? 'http://localhost:8888/api/emaillist' : 'http://192.168.0.172:8888/api/emaillist'),
                REFRESH_TOKEN_ENDPOINT: JSON.stringify('/refresh-token'),
                AUTHORIZATION_ENDPOINT: JSON.stringify('/oauth2/authorize/emaillist-oauth2-client')
            })                
        ],
        devtool: "eval-source-map",
        devServer: {
            host: '0.0.0.0',
            port: 9090,
            liveReload: true,
            compress: true,
            hot: false,
            proxy: {
                '/api': 'http://localhost:8888'
            },
            static: {
                directory: path.resolve('./public')
            },
            historyApiFallback: true
        }   
    };
}