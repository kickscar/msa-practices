import React, {useState, useEffect} from 'react';
import { useOutletContext } from 'react-router';
import { useNavigate } from 'react-router-dom';
import { useAuthContext, useAuthFetch } from '../../auth';
import RegisterForm from './RegisterForm';
import SearchBar from './SearchBar';
import Emaillist from './Emaillist';
import Header from './Header';
// import data from './assets/json/data';

function EmaillistApp() {
    let {token, storeToken} = useAuthContext();
    const {username, roles} = useOutletContext();
    const [emails, setEmails] = useState(null);
    const navigate = useNavigate();

    const addEmail = useAuthFetch(API_BASE_ENDPOINT, {method: 'post'});
    const fetchEmails = useAuthFetch(API_BASE_ENDPOINT);

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

            storeToken(null, true);

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

    // const addEmail = async (email) => {
    //     try {
    //         const response = await fetch(API_BASE_ENDPOINT, {
    //             method: 'post',
    //             headers: {
    //                 'Authorization': `Bearer ${token}`,
    //                 'Content-Type': 'application/json',
    //                 'Accept': 'application/json'
    //             },
    //             body: JSON.stringify(email)
    //         });

    //         if(!response.ok) {
    //             throw new Error(`${response.status} ${response.statusText}`)
    //         }

    //         const json = await response.json();

    //         if(json.result !== 'success') {
    //             throw new Error(`${json.result} ${json.message}`)
    //         }

    //         setEmails([json.data, ...emails]);
    //     } catch(err) {
    //         // 통신 에러가 나면 error 컴포넌트로 돌리고
    //         // 개발 중에는 화면에 내용 확인!
    //         // console.* 함수들은 development mode 일때는 작동하지만 production 모드일 때는 작동 안함(src/index.js 확인)
    //         console.error(err);
    //         navigate("/error");
    //     }
    // };


    // const fetchEmails2 = async (keyword) => {
    //     try {
    //         const response = await fetch(`${API_BASE_ENDPOINT}?kw=${keyword ? keyword : ''}`, {
    //             method: 'get',
    //             headers: {
    //                 'Authorization': `Bearer ${token}`,
    //                 'Content-Type': 'application/json',
    //                 'Accept': 'application/json'
    //             },
    //             body: null
    //         });

    //         if(response.status === 401) { // Unauthorized (Invalid or Expired Token)
    //             const response = await fetch(REFRESH_TOKEN_ENDPOINT, {method: 'get', headers: {'Accept': 'application/json', credentials: 'include'}}); 
    //             const json = await response.json();

    //             storeToken(token = json.data);
    //             ((delay) => new Promise((resolve) => setTimeout(resolve, delay)))(2000).then(() => fetchEmails(keyword));
    //             return;
    //         }

    //         if(!response.ok) {
    //             throw new Error(`${response.status} ${response.statusText}`)
    //         }

    //         const json = await response.json();

    //         if(json.result !== 'success') {
    //             throw new Error(`${json.result} ${json.message}`)
    //         }

    //         setEmails(json.data);
    //     } catch(err) {
    //         // 통신 에러가 나면 error 컴포넌트로 돌리고
    //         // 개발 중에는 화면에 내용 확인!
    //         // console.* 함수들은 development mode 일때는 작동하지만 production 모드일 때는 작동 안함(src/index.js 확인)
    //         console.error(err);
    //         navigate("/error");
    //     }
    // };

    useEffect(() => {
        fetchEmails().then(json => setEmails(json.data));
    }, []);

    return (
        <div id={'App'}>
            <Header
                name={username}
                logout={logout} />
            {  
                roles.includes("WRITE") ?  
                    <RegisterForm addEmail={async (email) => {
                        const json = await addEmail(email);
                        setEmails([json.data, ...emails]);
                    }} /> :
                    null
            }
            <SearchBar fetchEmails={async (keyword) => {
                const json = await fetchEmails({kw: keyword});
                setEmails(json.data);
            }}/>
            { 
                emails !== null ? 
                    <Emaillist
                        emails={emails}
                        deleteEmail={deleteEmail} /> :
                    null
            }
        </div>
    );
}

export default EmaillistApp;