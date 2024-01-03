import React from 'react';
import { useNavigate } from 'react-router-dom';
import styles from '../../assets/scss/Error.scss';

function Error404() {
    const navigate = useNavigate();
    
    return (
        <div id={'App'} className={styles.error404}>
            <h1>404 Not Found</h1>
            <p className={styles.desc}>
                로그인 유무와 상관 없이 렌더링 되는 컴포넌트 예제(주로 에러 페이지)
            </p>
            <a onClick={(event) => {
                event.preventDefault();
                navigate('/');
            }}>메인으로</a>
        </div>
    );
}

export default Error404;