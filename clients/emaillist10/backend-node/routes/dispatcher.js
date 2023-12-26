const router = require('express').Router();
const { controllerMain } = require('../controllers');

exports.dispatcher = router.get('/', controllerMain.index);