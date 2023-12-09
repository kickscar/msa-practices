import React from 'react';
import { NavLink } from "react-router-dom";
import { useAuthContext } from '../auth';

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
                <button onClick={() => {
                    setTimeout(() => {
                        storeToken('ThisIsTestToken');
                    }, 2000);
                }}>로그인</button>
            </div>
    );
}