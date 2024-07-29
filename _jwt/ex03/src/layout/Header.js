import React from 'react';
import {useAuthContext} from '../auth';

export default function Header() {
    const {token} = useAuthContext();

    return (
        <>
            <h1>Header</h1>
            <div>
                token: {!token ? 'none' : token}
            </div>
        </>
    );
}