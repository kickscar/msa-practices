import React from 'react';
import { NavLink } from "react-router-dom";
import { useAuthContext } from '../../auth';

export default function Signin() {
    const { storeToken } = useAuthContext();

    return (
        <div>
            <h1>로그인 페이지</h1>
            <NavLink to={`/`}>홈</NavLink>
            <ul>
                <li>
                    <NavLink to={`/signin`}>로그인</NavLink>
                </li>
                <li>
                    <NavLink to={`/signup`}>회원가입</NavLink>
                </li>
            </ul>
            <button onClick={async () => {
                try {
                    const credentials = {
                        email: 'kickscar@gmail.com',
                        password: '1234'
                    };

                    const response = await fetch('/api/auth', {
                        method: 'post',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        },
                        body: JSON.stringify(credentials)
                    });

                    if (!response.ok) {
                        throw new Error(`${response.status} ${response.statusText}`);
                    }

                    const json = await response.json();

                    if (json.result !== 'success') {
                        throw new Error(`${json.result} ${json.message}`);
                    }

                    console.log(json);
                    storeToken(json.data.accessToken);

                } catch (error) {
                    console.error(error);
                }
            }}>로그인</button>
            {' '}
            <button onClick={async () => {
                try {
                    const response = await fetch('/api/hb', {
                        method: 'get',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        },
                        body: null
                    });

                    if (!response.ok) {
                        throw new Error(`${response.status} ${response.statusText}`);
                    }

                    const json = await response.json();

                    console.log(json.data);

                } catch (error) {
                    console.error(error);
                }
            }}>Heartbeat!!</button>
        </div>
    );
}