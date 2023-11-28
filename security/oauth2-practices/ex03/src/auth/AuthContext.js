import React, {createContext, useState, useContext} from 'react';

const _AuthContext = createContext('');

export const AuthContext = ({ children }) => {
    const _tokenState = useState(null);

    return (
        <_AuthContext.Provider value={{ 
            token: _tokenState[0], 
            storeToken: _tokenState[1]
        }}>
            {children}
        </_AuthContext.Provider>
    );
}

// Auth Context Hook
export const useAuthContext = () => {
    return useContext(_authContext);
}
