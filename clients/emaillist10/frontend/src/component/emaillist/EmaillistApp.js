import React, {useState, useEffect} from 'react';
import { useOutletContext } from 'react-router';
import { useNavigate } from 'react-router-dom';
import { useAuthContext } from '../../auth';
import RegisterForm from './RegisterForm';
import SearchBar from './SearchBar';
import Emaillist from './Emaillist';
import Header from './Header';
// import data from './assets/json/data';

function EmaillistApp() {
    const { storeToken } = useAuthContext();
    const { token, claims } = useOutletContext();
    const [emails, setEmails] = useState(null);
    const navigate = useNavigate();

    const logout = async() => {
        try {
            const response = await fetch('/logout', {
                method: 'get',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            });

            if(!response.ok) {
                throw new Error(`${response.status} ${response.statusText}`)
            }

            const json = await response.json();

            if(json.result !== 'success') {
                throw new Error(`${json.result} ${json.message}`)
            }

            storeToken(null);
            navigate('/');
        } catch(err) {
            console.error(err);
        }
    };

    const addEmail = async (email) => {
        try {
            const response = await fetch(API_BASE_ENDPOINT, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(email)
            });

            if(!response.ok) {
                throw new Error(`${response.status} ${response.statusText}`)
            }

            const json = await response.json();

            if(json.result !== 'success') {
                throw new Error(`${json.result} ${json.message}`)
            }

            setEmails([json.data, ...emails]);
        } catch(err) {
            console.error(err);
        }
    };

    const fetchEmails = async (keyword) => {
        try {
            const response = await fetch(`${API_BASE_ENDPOINT}?kw=${keyword ? keyword : ''}`, {
                method: 'get',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: null
            });

            if(!response.ok) {
                throw new Error(`${response.status} ${response.statusText}`)
            }

            const json = await response.json();

            if(json.result !== 'success') {
                throw new Error(`${json.result} ${json.message}`)
            }

            setEmails(json.data);
        } catch(err) {
            console.error(err);
        }
    };

    useEffect(() => {
        fetchEmails();
    }, []);

    return (
        <div id={'App'}>
            <Header
                name={claims.preferred_username}
                logout={logout} />
            <RegisterForm addEmail={addEmail} />
            <SearchBar fetchEmails={fetchEmails}/>
            { emails === null ?
                null :
                <Emaillist emails={emails} />
            }
        </div>
    );
}

export default EmaillistApp;