const http = require('http');
const path = require('path');
const dotenv = require('dotenv');
const express = require('express');
const cookieParser = require('cookie-parser');

// application environment variables
dotenv.config({ path: path.join(__dirname, `config/app${process.env.NODE_ENV ? '-' + process.env.NODE_ENV : ''}.env`) });

// web application based on express
const app = express();

// application setup
app
  .use(express.static(path.join(__dirname, process.env.STATIC_RESOURCES_DIRECTORY)))
  .set('views', path.join(__dirname, 'views'))
  .set('view engine', 'ejs')
  .use(express.json())
  .use(express.urlencoded({ extended: true }))
  .use(cookieParser());

// build application router
const buildRouter = require('./routes');
buildRouter(app);

// 9. server startup
http
  .createServer(app)
  .on('listening', () => console.info(`Listening on port ${process.env.PORT}`))
  .on('error', (error) => {
    if (error.syscall !== 'listen') {
      throw error;
    }

    switch (error.code) {
      case 'EACCES':
        console.error(`Port ${process.env.PORT} requires elevated privileges`);
        process.exit(1);
        break;
      case 'EADDRINUSE':
        console.error(`Port ${process.env.PORT} is already in use`);
        process.exit(1);
        break;
      default:
        throw error;
    }
  })
  .listen(process.env.PORT);