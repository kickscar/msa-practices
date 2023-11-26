## JWT: ex03-frontend

#### install packages
```sh
$ npm init -y
$ npm i -D webpack webpack-cli webpack-dev-server style-loader css-loader node-sass sass-loader babel-loader @babel/core @babel/cli @babel/preset-env @babel/preset-react @babel/plugin-transform-runtime @babel/plugin-syntax-throw-expressions concurrently

$ npm i react react-dom react-router react-router-dom jwt-decode
```

#### config
1) webpack
   
   config/webpack.config.js

2) babel
   
   config/babel.config.json

#### run
1) run server
   ```sh
   $ npm run dev:be
   ```

2) run webpack dev server
   ```sh
   $ npm run dev:fe
   ```

3) run concurrently
   ```sh
   $ npm run dev
   ```


