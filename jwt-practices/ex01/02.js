const jwt = require('jsonwebtoken');
require("dotenv").config();


try {
    const options = {
        algorithm: 'HS256'
    };

    let token = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options);
    token = token.toUpperCase();

    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);
} catch (error) {
    console.error(error);
}
