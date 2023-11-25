import React from 'react';
import { Route, RouterProvider, createBrowserRouter } from 'react-router-dom';
import { AuthContextProvider } from './AuthContextProvider';
import { AuthAccountContext, AuthNotRequired, AuthRequired } from './AuthNesting';
import { AuthRoutes } from './AuthRoutes';

export const AuthContextRouter = ({ children }) => {
    const routesPublic =  {
        element: <AuthAccountContext />,
        path: '/',
        children: []
    };
    const routesAuthNotRequired = {
        path: '/',
        element: <AuthNotRequired />,
        children: []
    };
    const routesAuthRequired = {
        path: '/',
        element: <AuthRequired />,
        children: []
    };

    const routesRecursiveBuild = (c, r) => {
        if(!r.props) {
            c.push(r);
            return;
        }

        (r.props.children instanceof Array ? r.props.children : r.props.children.type === Route ? [r.props.children] : []).forEach(route => {
            const n = Object.assign({}, route.props);

            if(route.props.children) {
                n.children = [];
                routesRecursiveBuild(n.children, route.props.children instanceof Array ? route /* Route Array */ : route.props.children.props /* Route */ );
            }

            routesRecursiveBuild(c, n);            
        });
    };
    
    (children instanceof Array ? children : [children.props.children]).forEach(routes => {
        const routeContainer = routes.type !== AuthRoutes ? routesPublic.children : routes.props.authenticated ? routesAuthRequired.children : routesAuthNotRequired.children;
        routesRecursiveBuild(routeContainer, routes);
    });

    const browserRouter = createBrowserRouter([routesPublic, routesAuthNotRequired, routesAuthRequired]);
    return (
        <AuthContextProvider>
            <RouterProvider router={browserRouter} />
        </AuthContextProvider>
    );
};