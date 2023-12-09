const jwt = require('jsonwebtoken');
require("dotenv").config();

//
// JsonWebTokenError: invalid signature
//


try {
    const options = {
        algorithm: 'HS256'
    };

    const token = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options);

    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET_OLD);
} catch (error) {
    console.error(error);
}
