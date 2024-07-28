import React from 'react';
import {BrowserRouter as Router} from 'react-router-dom';
import {Routes, Route} from 'react-router';
import {MySiteLayout} from './layout';
import {Main} from './component/main';
import {Guestbook} from './component/guestbook';
import {Gallery} from './component/gallery';
import {SignIn, SignUp, Settings} from './component/user';
import {Error404} from './component/error';
import './assets/scss/App.scss';

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path='/' element={<MySiteLayout /> }>
                    <Route index path='' element={<Main />}/>
                    <Route path='/gallery' element={<Gallery />}/>
                    <Route path='/guestbook' element={<Guestbook />}/>
                    <Route path='/user/join' element={<SignUp />}/>
                    <Route path='/user/login' element={<SignIn />}/>
                    <Route path='/user/settings' element={<Settings />}/>
                    <Route path='*' element={<Error404 />}/>
                </Route>
            </Routes>
        </Router>
    );
}