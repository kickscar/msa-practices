import React from 'react';
import './assets/scss/App.scss';
import { Routes, Route } from 'react-router';
import { AuthContextRouter, AuthRoutes } from './auth';
import { Welcome } from './component/main';
import { EmaillistApp } from './component/emaillist';
import { Error, Error404 } from './component/error';

function App() {
    return (
        <AuthContextRouter>
            <AuthRoutes>
                <Route path={'/welcome'} element={<Welcome />} />
            </AuthRoutes>
            
            <AuthRoutes authenticated>
                <Route path={'/'} index element={<EmaillistApp />} />
            </AuthRoutes>

            <Routes>
                <Route path={'/error'} element={<Error />} />
                <Route path={'/*'} element={<Error404 />} />
            </Routes>
        </AuthContextRouter>
    );
}

export {App};