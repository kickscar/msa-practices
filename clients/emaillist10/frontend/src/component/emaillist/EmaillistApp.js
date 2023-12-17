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
    const { token, username, roles } = useOutletContext();
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
            // 통신 에러가 나면 error 컴포넌트로 돌리고
            // 개발 중에는 화면에 내용 확인!
            // console.* 함수들은 development mode 일때는 작동하지만 production 모드일 때는 작동 안함(src/index.js 확인)
            console.error(err);
            navigate("/error");
        }
    };

    const deleteEmail = async (no) => {
        console.log('delete ' + no);
    }

    const addEmail = async (email) => {
        try {
            const response = await fetch(API_BASE_ENDPOINT, {
                method: 'post',
                headers: {
                    'Authorization': `Bearer ${token}`,
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
            // 통신 에러가 나면 error 컴포넌트로 돌리고
            // 개발 중에는 화면에 내용 확인!
            // console.* 함수들은 development mode 일때는 작동하지만 production 모드일 때는 작동 안함(src/index.js 확인)
            console.error(err);
            navigate("/error");
        }
    };

    const fetchEmails = async (keyword) => {
        try {
            const response = await fetch(`${API_BASE_ENDPOINT}?kw=${keyword ? keyword : ''}`, {
                method: 'get',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: null
            });

            if(response.status === 401) {
                console.log("<----- Invalid Token or Expired!! ----->");
                location.reload();
                return;
            }

            if(!response.ok) {
                throw new Error(`${response.status} ${response.statusText}`)
            }

            const json = await response.json();

            if(json.result !== 'success') {
                throw new Error(`${json.result} ${json.message}`)
            }

            setEmails(json.data);
        } catch(err) {
            // 통신 에러가 나면 error 컴포넌트로 돌리고
            // 개발 중에는 화면에 내용 확인!
            // console.* 함수들은 development mode 일때는 작동하지만 production 모드일 때는 작동 안함(src/index.js 확인)
            console.error(err);
            navigate("/error");
        }
    };

    useEffect(() => {
        fetchEmails();
    }, []);

    return (
        <div id={'App'}>
            <Header
                name={username}

                logout={logout} />
            {  
                roles.includes("WRITE") ?  
                    <RegisterForm addEmail={addEmail} /> :
                    null
            }
            <SearchBar fetchEmails={fetchEmails}/>
            { emails === null ?
                null :
                <Emaillist emails={emails} deleteEmail={deleteEmail} />
            }
        </div>
    );
}

export default EmaillistApp;