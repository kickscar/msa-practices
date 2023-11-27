1. (J)son (W)eb (T)oken 기본

2. JWT 서버 기본 구현: Node

3. 
## JWT: ex01: Basics

#### basic-jwt.js

#### basic-server.js

#### install packages
1) dev. tools
   ```sh
   $ npm i -D nodemon
   ```

2) jwt & express
   ```sh
   $ npm i jsonwebtoken express dotenv  
   ```


#### config
1) .env
   - seceret key for access token
   - seceret key for referh token


#### run
1) jsonwebtoken basic test
   ```sh
   $ node basic-jwt
   ```
2) server
   ```sh
   $ npm run dev
   ```

       [Client(Browser)]  |                                                   |  [JWT Server]
                          |                                                   |
                          |                                                   |
                          |                                                   |
                          |  1./auth                                          |
                          | ------------------------------------------------> |
                          |    {                                              |
                          |       "username": "...",                          |
                          |       "password": "..."                           |
                          |    }                                              |  2.Validate Credentials
                          |                                                   |  3.Issue Tokens(Access Token, Fresh Token) 
                          |                                                   |
                          |                                                   |
                          |                                       4.res(200)  |
                          | <------------------------------------------------ |
                          |                        {                          |
                          |                           "accessToken": "...",   |
                          |                           "refreshToken": "..."   |
                          |                        }                          |
                          |                                                   |
                          |                                                   |
  5.Store Tokens Locally  |                                                   |
                          |                                                   |
                          |                                                   |
                          |  6./profile (call apis)                           |
                          | ------------------------------------------------> |
                          |   Authorization: Bearer accessToken               |               
                          |   Accept: application/json                        |
                          |                                                   |
                          |                                                   |  7-1.Verify Access Token(Validate Signature) 
                          |                                                   |
                          |                                     8-1.res(200)  |
                          | <------------------------------------------------ |
                          |                                  { json result }  |
                          |                                                   |  
                          |                                                   |  or 
                          |                                                   |   
                          |                                                   |
                          |                                                   |  7-2. Access Token Expired
                          |                                                   |
                          |                                     8-2.res(401)  |  
                          | <------------------------------------------------ |
                          |                                                   |
                          |                                                   |
                          |                                                   |
                          |                                                   |
                          |                                                   |
                          |  9./refresh-token                                 |
                          | ------------------------------------------------> |
                          |    {                                              |
                          |       "refreshToken": "..."                       |
                          |    }                                              |
                          |                                                   |
                          |                                                   |  10.Verify Refresh Token(Validate Signature)
                          |                                                   |  11.Issue Tokens(Access Token, Fresh Token) 
                          |                                                   |
                          |                                       12.res(200) |
                          | <------------------------------------------------ |
                          |                        {                          |
                          |                           "accessToken": "...",   |
                          |                           "refreshToken": "..."   |
                          |                        }                          |
                          |                                                   |
                          |                                                   |
 13.Store Tokens Locally  |                                                   |
                          |                                                   |








+---------------------------------------------------------+
|                                                         |          
|  Servlet Context(Tomcat)                                |            +----------------------------------------------------------------------------------------+
|                                                         |            |                                                                                        |
|                                                         |            |  Application Context(Spring Container)                                                 |
|      DelegatingFilterProxy: Filter                      |            |                                                                                        |
|     +---------------------------------------------+     |            |                                                                                        |
|     | urlPattern: "/*"                            |     |            |      FilterChainProxy("springSecurityFilterChain"): Filter                             |
|     | targetBeanName: "springSecurityFilterChain" |     |            |     +---------------------------------------------------------------------------+      |
|     |---------------------------------------------|     |  delegate  |     |                                                                           |      |
|     | doFilter()                                  | ---------------------> | doFilter()                                                                |      |
|     +---------------------------------------------+     |            |     |    |                                                                      |      |
|                                                         |            |     |    |-> SecurityFilterChain1                                               |      |
|                                                         |            |     |    |   "/assets/**": []                                                   |      |
+---------------------------------------------------------+            |     |    |                                                                      |      |
                                                                       |     |    |-> SecurityFilterChain2                                               |      |
                                                                       |     |        "/**": [SecurityFilter1, SecurittFilter2, SecurityFilter3, ...]    |      |     
                                                                       |     |                                                                           |      |     
                                                                       |     +---------------------------------------------------------------------------+      |
                                                                       |                                                                                        |
                                                                       |                                                                                        |
                                                                       +----------------------------------------------------------------------------------------+











   