import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext('');

export const AuthContextProvider = ({ children }) => {
    // 새 access token 발급받음: 반드시 동기 통신!
    syncFetchToken();

    // 발급받은 새 access token을 Context State 으로 저장
    const tokenState = useState(ACCESSTOKEN);

    return (
        <AuthContext.Provider value={{
            token: tokenState[0],
            storeToken: tokenState[1]
        }}>
            {children}
        </AuthContext.Provider>
    );
}

// Auth Context Hook
export const useAuthContext = () => {
    return useContext(AuthContext);
}

// Fetch new access token issued with refresh token based (synchronous fetch) 
var ACCESSTOKEN;

const syncFetchToken = () => {
    const xhr = new XMLHttpRequest();

    xhr.addEventListener('load', () => {
        if (xhr.status !== 200) {
            console.error(`${xhr.responseURL} ${xhr.status} (${xhr.statusText})`);
            return;
        }

        const json = JSON.parse(xhr.responseText);
        if (!json.data) {
            // cookie에 refresh token이 없거나(after logout 또는 first start), 또는 기간이 만료, 또는 유효하지 않은 refresh token를 cookie로 보냈음.
            console.log('Access token could not be issued with refresh token: EMPTY(logout or first start), EXPIRED or INVALID refresh token');
            return;
        }

        // 정상적으로 발급받은 access token을 메모리(전역변수)에 저장.
        ACCESSTOKEN = json.data;
        console.log(`access token issued: ${ACCESSTOKEN}`)
    });

    xhr.open('get', REFRESH_TOKEN_ENDPOINT, false);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.send();
};