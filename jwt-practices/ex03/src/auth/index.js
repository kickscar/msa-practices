import React, {createContext, useState, useContext} from 'react';
import {Navigate, Outlet, RouterProvider, createBrowserRouter} from "react-router-dom";

const _AuthContext = createContext("");

const _AuthContextProvider = ({children}) => {
    const _token = useState(null);
    return (
        <_AuthContext.Provider value={{
            token: _token[0],
            storeToken: _token[1]
        }}>
            {children}
        </_AuthContext.Provider>
    );
}

const _AuthRequired = () => {
    const {token} = useAuthContext();
    return !token ? <Navigate to="/signin" /> : <Outlet />; 
}

const _AuthNotRequired = () => {
    const {token} = useAuthContext();

    if(!token) {
        return <Outlet />;
    }
    
    return <Navigate to="/bella" />
}

export const AuthContextRouter = ({children}) => {
    const routesPublic = [];
    const routesAuthNotRequired = {
        path: "/",
        element: <_AuthNotRequired />,
        children: []
    };
    const routesAuthRequired = {
        path: "/",
        element: <_AuthRequired />,
        children: []
    };

    (children instanceof Array ? children : [children.props.children]).forEach(routes => {
        const routesSrc = routes.props.children instanceof Array ? routes.props.children : [routes.props.children];
        const routesDest = routes.type !== AuthRoutes ? routesPublic : routes.props.authenticated ? routesAuthRequired.children : routesAuthNotRequired.children;

        routesSrc.forEach(route => {
            routesDest.push(route.props);
        });
    });

    const browserRouter = createBrowserRouter([...routesPublic, routesAuthNotRequired, routesAuthRequired]);
    return (
        <_AuthContextProvider> 
            <RouterProvider router={browserRouter} />
        </_AuthContextProvider>
    );
};

export const AuthRoutes = ({children}) => {
    return (
        children
    );
}

export const useAuthContext = () => {
    return useContext(_AuthContext);
}

