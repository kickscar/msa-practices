const express = require('express');
const { controllerMain, controllerOAuth2} = require('../controllers');

const router = express.Router();

router.get('/', controllerMain.index);

/* OAuth2 APIs */
router.get('/refresh-token', controllerOAuth2.refreshToken);
router.get('/logout', controllerOAuth2.logout);

exports.dispatcher = router;