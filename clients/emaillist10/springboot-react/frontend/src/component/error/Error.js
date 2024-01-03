import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../assets/scss/Error.scss';

function Error() {
    const navigate = useNavigate();

    return (
        <div id={'App'} className={styles.error}>
            <h1>Sorry but Error</h1>
            <p className={styles.desc}>
            사용 중에 불편을 어쩌고... 저쩌고...
            </p>
            <a onClick={(event) => {
                event.preventDefault();
                navigate('/');
            }}>메인으로</a>
        </div>
    );
}

export default Error;