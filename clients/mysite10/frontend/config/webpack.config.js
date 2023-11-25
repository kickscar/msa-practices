const path = require('path');
const webpack = require('webpack');
const CaseSensitivePathsPlugin = require('case-sensitive-paths-webpack-plugin');

module.exports = (env) => ({ 
    mode: "none",
    entry: path.resolve('src/index.js'),
    output: {
        path: path.resolve('../backend/src/main/resources'),
        filename: 'assets/js/main.js',
        assetModuleFilename: 'assets/images/[hash][ext]'
    },
    module: {
        rules: [{
            test: /\.(png|gif|jpe?g|svg|ico|tif?f|bmp)$/i,
            type: 'asset/resource'
        }, {
            test: /\.(sa|sc|c)ss$/i,
            use: [
                'style-loader',
                {loader: 'css-loader', options: {modules: true}},
                'sass-loader'
            ]
        }, {
            test: /\.js$/,
            exclude: /node_modules/,
            loader: 'babel-loader',
            options: {
                configFile: path.resolve('config/babel.config.json')
            }
        }]
    },
    plugins: [
        new CaseSensitivePathsPlugin(),
        new webpack.DefinePlugin({
            'process.env':{
              'API_URL': JSON.stringify(process.env.NODE_ENV === 'development' ? 'http://localhost:8888' : 'http://192.168.0.172:8888') 
            }
          })
    ],
    devtool: "eval-source-map",
    devServer: {
        host: "0.0.0.0",
        port: 9090,
        liveReload: true,
        hot: false,
        compress: true,
        static: {
            directory: path.resolve('./public')
        },
        historyApiFallback: true
    }    
});