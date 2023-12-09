import React from 'react';
import {useParams, NavLink} from "react-router-dom";
import {useAuthContext} from '../auth';
import {useNavigate} from "react-router-dom";

export default function Navigation() {
    const {accountName} = useParams();
    const {storeToken} = useAuthContext();
    const navigate = useNavigate();

    return (
        <>
            <NavLink to={`/`}>홈</NavLink> 
            <ul>
                <li><NavLink to={`/profile`}>프로필</NavLink></li>
                <li><NavLink to={`/guestbook`}>방명록</NavLink></li>
                <li><NavLink to={`/gallery`}>갤러리</NavLink></li>
            </ul>
            <p>
                <button onClick={() => {
                    setTimeout(() => {
                        storeToken(null);
                        navigate("/");
                    }, 2000);
                }}>로그아웃</button>
            </p>
        </>
    );
}