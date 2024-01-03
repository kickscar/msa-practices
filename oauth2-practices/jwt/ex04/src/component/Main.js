import React from 'react';
import { NavLink } from "react-router-dom";

export default function Main() {
    return (
            <div>
                <h1>MySite</h1>
                <NavLink to={`/`}>홈</NavLink>
                <ul>
                    <li>
                        <NavLink to={`/signin`}>로그인</NavLink>
                    </li>
                    <li>
                        <NavLink to={`/signup`}>회원가입</NavLink>
                    </li>
                </ul>
            </div>
    );
}