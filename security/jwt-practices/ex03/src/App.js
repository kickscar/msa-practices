import React from 'react';
import { AuthContextProvider } from './auth';
import { Signin } from './component';

export default function App() {
    return (
        <AuthContextProvider>
            <Signin />
        </AuthContextProvider>
    );
}