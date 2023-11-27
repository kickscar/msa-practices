import React from 'react';
import { NavLink } from "react-router-dom";

export default function Index() {
    return (
        <div>
            <h1>My Portfolio</h1>
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