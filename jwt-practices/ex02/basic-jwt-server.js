const http = require("http");
const express = require("express");
const bodyParser = require("body-parser");
const jwt = require("jsonwebtoken");
require("dotenv").config();

const PORT = 9090;
const app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

//
//  request: /auth
//
app.post("/auth", (req, res) => {
    const account = {
        id: 1,
        name: 'kickscar',
        profileImage: ''
    };

    const generateAccessTokenOptions = {
        algorithm: 'HS256',
        expiresIn: '1m'
    };

    const generateRefreshTokenOptions = {
        algorithm: 'HS256',
        expiresIn: '180d'   //180 days
    };

    const accessToken = jwt.sign(account,  process.env.ACCESS_TOKEN_SECRET, generateAccessTokenOptions);
    const refreshToken = jwt.sign(account,  process.env.REFRESH_TOKEN_SECRET, generateRefreshTokenOptions);

    res.json({accessToken, refreshToken});
});

//
//  request: /refresh-token
//
app.post("/refresh-token", async (req, res) => {
    try {
        const refreshToken = req.body.refreshToken;
        if(!refreshToken) {
            return res.sendStatus(401);
        }

        const generateAccessTokenOptions = {
            algorithm: 'HS256',
            expiresIn: '10m'
        };

        const verified = await jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET);
        const {iat, exp, ...account} = verified;
        const accessToken = jwt.sign(account, process.env.ACCESS_TOKEN_SECRET, generateAccessTokenOptions);

        res.json({accessToken});

    } catch (error) {
        console.log(error);
        return res.sendStatus(403);
    }
});


//
//  request: /profile
//
app.get("/profile", async (req, res, next) => {
    try {
        const authHeader = req.headers["authorization"];
        const token = authHeader?.split(" ")[1];

        if (!token) {
            console.log("wrong token format or token is not sended");
            return res.sendStatus(400);
        }

        const verified = await jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);
        req.accountId = verified.id;

        next?.();
    } catch(error) {
        console.log(error);
        return res.sendStatus(403);
    } 

}, (req, res) => {
    res.json({result: "success", data: req.accountId, message: null});
});


// starts...
http.createServer(app).listen(PORT, () => {
    console.log(`Server running on ${PORT}`);
});