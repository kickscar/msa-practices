import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuthContext } from './AuthContextProvider';
import { jwtDecode } from 'jwt-decode';

export const AuthRequired = () => {
    const {token} = useAuthContext();

    if(!token) {
        location.href = AUTHORIZATION_ENDPOINT;
        return;
    }

    return <Outlet context={{
        token,
        claims: jwtDecode(token)
    }}/>; 
}

export const AuthNotRequired = () => {
    const {token} = useAuthContext();

    if(!token) {
        return <Outlet />;
    }
    
    return <Navigate to="/" />
}