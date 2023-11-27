import React from 'react';
import {Routes, Route} from 'react-router';
import {AuthContextRouter, AuthRoutes} from './auth';

import Main from './component/main';
import Signin from './component/main/Signin';
import Signup from './component/main/Signup';

import Education from "./component/account/Education";
import Profile from "./component/account/Profile";
import Experience from "./component/account/Experience";
import Training from "./component/account/Training";

export default function App() {
    return (
        <AuthContextRouter>
            <AuthRoutes>
                <Route path='/' element={<Main />} />
                <Route path='/signin' element={<Signin />} />
                <Route path='/signup' element={<Signup />} />
            </AuthRoutes>
            <AuthRoutes authenticated>
                <Route path='/:accountName' element={<Profile />} />
                <Route path='/:accountName/profile' element={<Profile />} />
                <Route path='/:accountName/education' element={<Education />} />
                <Route path='/:accountName/experience' element={<Experience />} />
                <Route path='/:accountName/training' element={<Training />} />
            </AuthRoutes>
            <Routes>
                <Route path='/public/test01' element={<div><h1>public/test01</h1></div>} />
            </Routes>            
        </AuthContextRouter>
    );
}