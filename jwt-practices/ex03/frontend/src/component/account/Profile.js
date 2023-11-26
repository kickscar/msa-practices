import React, {useEffect, useState} from 'react';
import {useParams, useNavigate, useLocation} from "react-router-dom";
import {useAuthContext} from '../../auth';
import LayoutAccount from "../../layout/LayoutAccount";

export default function Profile() {
    console.log("profile rendering");

    const {accountName} = useParams();
    const {token, storeToken} = useAuthContext();
    const [profile, setProfile] = useState(null);

    const navigate = useNavigate();
    const location = useLocation();


    const fetchProfile = async () => {
        try {
            const response = await fetch(`/api/${accountName}/profile`, {
                method: 'get',
                redirect: "follow",
                headers: {
                    'Authorization': `bearer ${token}`,
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: null
            });

            if (!response.ok) {
                throw new Error(`${response.status} ${response.statusText}`);
            }   

            const json = await response.json();
            if (json.result !== 'success') {
                throw new Error(`${json.result} ${json.message}`);
            }

            const accessTokenRefreshedAt = response.headers.get('X-Mypofol-Refresh-Token-At');

            // Access Token이 재발급 되었으면,
            if(accessTokenRefreshedAt) {
                storeToken(json.data.accessToken);
                return;
            }

            setProfile(json.data.profile);

        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        console.log("mount");
        fetchProfile();
    }, [token]);

    return (
        <LayoutAccount>
            <div>
                <h1>{accountName}의 Profile 페이지</h1>
                {
                    profile ? 
                        <>
                            <h4>{profile.name}</h4>
                            <h5>{profile.email}</h5>
                            <h6>{profile.phone}</h6>
                        </>
                        :
                        null
                }
            </div>
        </LayoutAccount>
    );
}