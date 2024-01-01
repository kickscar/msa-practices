import React from 'react';
import { useAuthContext } from '../auth';
import { Layout } from '../layout';

export default function Signin() {
    const {storeToken} = useAuthContext();
    return (
            <Layout>
                <h2>로그인</h2>
                <button onClick={() => {
                    setTimeout(() => {
                        storeToken('ThisIsTestToken');
                    }, 2000);
                }}>로그인</button>
            </Layout>
    );
}