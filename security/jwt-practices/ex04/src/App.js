import React from 'react';
import { Routes, Route } from 'react-router';
import { AuthContextRouter, AuthRoutes } from './auth';
import { Main, Signin, Signup, Profile, Guestbook, Gallery } from './component';


export default function App() {
    return (
        <AuthContextRouter>
            <AuthRoutes>
                <Route path='/' element={<Main />} />
                <Route path='/signin' element={<Signin />} />
                <Route path='/signup' element={<Signup />} />
            </AuthRoutes>
            <AuthRoutes authenticated>
                <Route path='/profile' element={<Profile />} />
                <Route path='/gallery' element={<Gallery />} />
                <Route path='/guestbook' element={<Guestbook />} />
            </AuthRoutes>
            <Routes>
                <Route path='/public/test01' element={
                    <div>
                        <h1>public/test01</h1>
                    </div>
                } />
            </Routes>            
        </AuthContextRouter>
    );
}