const jwt = require('jsonwebtoken');
require("dotenv").config();

//
// TokenExpiredError: jwt expired
//

(async () => {
    try {
        const options = {
            algorithm: 'HS256',
            expiresIn: '1s'     // 1초
        };

        var token = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options);
        console.log(token);

        await new Promise(resolve => setTimeout(resolve, 2000));

        const verified = jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);

    } catch (error) {
        console.log(error.name);

        const decoded = jwt.decode(token);
        console.log(decoded);

        const decodedComplete = jwt.decode(token, { complete: true });
        console.log(decodedComplete);
    }
})();