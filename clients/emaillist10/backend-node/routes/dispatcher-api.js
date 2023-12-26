const express = require('express');
// const { controllerAccount, controllerProfile, controllerExperiences, controllerEducations, controllerTrainings } = require('../controllers');

const router = express.Router();

/* APIs */
// router.post('/signup', controllerAccount.create);
// router.post('/auth', controllerJWT.auth);
// router.get('/refresh-token', controllerJWT.refreshToken);
// router.get('/signout', controllerJWT.signout);

exports.dispatcherApi = router;