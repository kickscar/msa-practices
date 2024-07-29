import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext('');

export const AuthContextProvider = ({ children }) => {
    const tokenState = useState(null);

    return (
        <AuthContext.Provider value={{
            token: tokenState[0],
            storeToken: tokenState[1]
        }}>
            {children}
        </AuthContext.Provider>
    );
}

// Auth Context Hook
export const useAuthContext = () => {
    return useContext(AuthContext);
}
