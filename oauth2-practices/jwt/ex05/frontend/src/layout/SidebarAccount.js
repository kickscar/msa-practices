import React from 'react';
import { useParams, NavLink } from "react-router-dom";
import { useAuthContext } from '../auth';
import { useNavigate } from "react-router-dom";

export default function Navigation() {
    const { accountName } = useParams();
    const { storeToken } = useAuthContext();
    const navigate = useNavigate();

    return (
        <>
            <NavLink to={`/`}>홈</NavLink>
            <ul>
                <li><NavLink to={`/${accountName}/profile`}>Profile</NavLink></li>
                <li><NavLink to={`/${accountName}/education`}>Education</NavLink></li>
                <li><NavLink to={`/${accountName}/experience`}>Experience</NavLink></li>
                <li><NavLink to={`/${accountName}/training`}>Training</NavLink></li>
            </ul>
            <p>
                <button onClick={ async () => {
                    try {
                        const response = await fetch('/api/signout', {
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

                        if (json.result !== 'success') {
                            throw new Error(`${json.result} ${json.message}`);
                        }

                        storeToken(null);
                        
                    } catch (error) {
                        console.error(error);
                    }                   
                }}>로그아웃</button> 
            </p>
        </>
    );
}