import React from 'react';
import {NavLink} from "react-router-dom";

export default function Signup() {
    return (
            <div>
                <h1>회원가입</h1>
                <NavLink to={`/`}>홈</NavLink>
                <ul>
                    <li>
                        <NavLink to={`/signin`}>로그인</NavLink>
                    </li>
                    <li>
                        <NavLink to={`/signup`}>회원가입</NavLink>
                    </li>
                </ul>
                <p>
                    생략
                </p>
            </div>
    );
}