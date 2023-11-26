const jwt = require('jsonwebtoken');
require("dotenv").config();

//
// sign & verify
//
function test01() {
    const options1 = {
        algorithm: 'HS256'
    };

    const options2 = {
        algorithm: 'HS256',
        expiresIn: '24h' // 24hours  
    };

    const token1 = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options1);
    const token2 = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options2);

    const verifyed1 = jwt.verify(token1, process.env.ACCESS_TOKEN_SECRET);
    console.log(token1);
    console.log(verifyed1);

    const verifyed2 = jwt.verify(token2, process.env.ACCESS_TOKEN_SECRET);
    console.log(token2);
    console.log(verifyed2);
}


//
// JsonWebTokenError: invalid token
//
function test02() {
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
}


//
// JsonWebTokenError: invalid signature
// 
function test03() {
    try {
        const options = {
            algorithm: 'HS256'
        };

        const token = jwt.sign({ id: 1, name: 'kickscar', profileImage: 'profile.jpg' }, process.env.ACCESS_TOKEN_SECRET, options);

        jwt.verify(token, process.env.ACCESS_TOKEN_SECRET_OLD);
    } catch (error) {
        console.error(error);
    }
}


//
// TokenExpiredError: jwt expired
//
const test04 = async () => {
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
}

// test01();
// test02();
// test03();
test04();