const http = require("http");
const express = require("express");

const bodyParser = require("body-parser");
const cookieParser = require('cookie-parser');
const jwt = require("jsonwebtoken");

require("dotenv").config();

const PORT = 8080;




/**
 * 
 *  Express Application: app 
 * 
 */
const app = express();

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(cookieParser());
//
// Middleware I: JsonResult 
//
app.use((req, res, next) => {
    try {
        const jsonResult = (obj) => obj instanceof Error ? {
            result: 'fail',
            data: null,
            message: obj.message
        } : {
            result: 'success',
            data: obj,
            message: null
        };

        const jsonOriginalFn = res.json;

        res.json = (param) => {
            if (param && param.then != undefined) { // Async Call
                return param.then((obj) => {
                    res.json = jsonOriginalFn;
                    return jsonOriginalFn.call(res, jsonResult(obj));
                }).catch((error) => {
                    next?.(error);
                });
            } else { // Non-Async Call
                res.json = jsonOriginalFn;
                return jsonOriginalFn.call(res, jsonResult(param));
            }
        }

        next?.();
    } catch (error) {
        next?.(error);
    }
});






/**
 * 
 *  Route & Controller 
 * 
 */

//  request: /api/auth
app.post("/api/auth", (req, res) => {
    const account = {
        id: 1,
        name: 'kickscar',
        profileImage: '/images/kickscar.png'
    };

    const accessToken = jwt.sign(account, process.env.ACCESS_TOKEN_SECRET, JSON.parse(process.env.ACCESS_TOKEN_GEN_OPTIONS));
    const refreshToken = jwt.sign(account, process.env.REFRESH_TOKEN_SECRET, JSON.parse(process.env.REFRESH_TOKEN_GEN_OPTIONS));
    
    res
        .cookie(process.env.REFRESH_TOKEN_COOKIE_NAME, refreshToken, JSON.parse(process.env.REFRESH_TOKEN_COOKIE_GEN_OPTIONS))
        .json({ accessToken });
});

//  request: /api/signout
app.get("/api/signout", (req, res) => {
    // clear cookie
    res
        .clearCookie(process.env.REFRESH_TOKEN_COOKIE_NAME)
        .json(null);
});

//  request: /api/hb
app.get("/api/hb", (req, res) => {
    res.json("lub dub!");
});

//  request: /api/refresh-token
app.get("/api/refresh-token", async (req, res) => {
    try {
        const cookieRefreshToken = req.cookies[process.env.REFRESH_TOKEN_COOKIE_NAME];

        if (!cookieRefreshToken) {
            throw new Error('Refresh Token Not Exist in Cookie');
        }
    
        const verified = await jwt.verify(cookieRefreshToken, process.env.REFRESH_TOKEN_SECRET);
        const {iat, exp, ...account} = verified;

        // 새로 발급! 
        const accessToken = jwt.sign(account, process.env.ACCESS_TOKEN_SECRET, JSON.parse(process.env.ACCESS_TOKEN_GEN_OPTIONS));
        const refreshToken = jwt.sign(account, process.env.REFRESH_TOKEN_SECRET, JSON.parse(process.env.REFRESH_TOKEN_GEN_OPTIONS));
        
        res
            .header({'X-Mypofol-Refresh-Token-At': new Date().toUTCString() })
            .cookie(process.env.REFRESH_TOKEN_COOKIE_NAME, refreshToken, JSON.parse(process.env.REFRESH_TOKEN_COOKIE_GEN_OPTIONS))
            .json({ accessToken });

    } catch (error) {
        // console.log(error);
        return res.json(null);
    }
});

//  request: /api/:accountName/profile, /api/:accountName
app.get(

    ['/api/:accountName', '/api/:accountName/profile'],

    /* verify token interceptor */
    async (req, res, next) => {
        try {
            const authHeader = req.headers["authorization"];
            const token = authHeader?.split(" ")[1];

            if (!token) {
                console.log("wrong token format or token is not sended");
                return res.status(400).json(null);
            }

            const verified = await jwt.verify(token, process.env.ACCESS_TOKEN_SECRET);
            req.authAccount = {
                id: verified.id
            };

            next?.();

        } catch (error) {
            return res.redirect('/api/refresh-token');
        }
    },


    /* valid account interceptor */
    (req, res, next) => {
        //
        // DB 확인 작업 생략
        // const account = modelAccount.findByName(req.params.accountName)
        //
        const account = {
            id: 1,
            name: req.params.accountName
        }

        req.account = account;

        next?.();
    },

    /* controller function */
    (req, res) => {
        //
        // DB select 작업 생략
        // const profile = modelProfile.findByAccountId(req.account.id)
        //
        const profile = {
            name: '안대혁',
            email: 'kickscar@gmail.com',
            phone: '010-4761-6934'
        };

        res.json({
            account: req.account,
            profile
        });
    }
);


// starts...
http.createServer(app).listen(PORT, () => {
    console.log(`Server running on ${PORT}`);
});