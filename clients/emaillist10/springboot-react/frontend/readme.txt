Emaillist: Frontend

1.  설치
    1)  개발툴
        $ npm i -D webpack webpack-cli webpack-dev-server css-loader style-loader sass-loader node-sass babel-loader @babel/core @babel/preset-env @babel/preset-react case-sensitive-paths-webpack-plugin @babel/plugin-transform-runtime @babel/plugin-syntax-throw-expressions cross-env nodemon
    2)  라이브러리
        $ npm i react react-dom prop-types react-addons-update jwt-decode react-router react-router-dom

2.  설정
    1)  webpack.config.js
    2)  babel.config.json

3.  스트립팅
    "scripts": {
        "build": "cross-env NODE_ENV=production npm install && npx webpack --config config/webpack.config.js --mode production",
        "dev": "cross-env NODE_ENV=development npx webpack serve --config config/webpack.config.js --progress --mode development",
        "dev:build": "nodemon --exec \"cross-env NODE_ENV=development npx webpack --config config/webpack.config.js --mode development\""
    }

4.  webpack 테스트 서버 실행
    $ npm run dev

5.  테스트 빌드: supporting spring boot devtools' livereload  
    $ npm run dev:build

6.  베포빌드
    $ npm run build
