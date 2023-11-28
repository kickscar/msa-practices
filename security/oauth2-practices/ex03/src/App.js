import React from 'react';
import { AuthContext } from './auth';
import { Signin } from './component';

export default function App() {
    return (
        <AuthContext>
            <Signin />
        </AuthContext>
    );
}