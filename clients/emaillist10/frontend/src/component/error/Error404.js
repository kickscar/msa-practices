import React from 'react';
import styles from '../../assets/scss/Error.scss';

function Error404(props) {
    return (
        <div id={'App'}>
            <h1>404 Not Found</h1>
            <p className={styles.desc}>
                로그인 유무와 상관 없이 렌더링 되는 컴포넌트 예제(주로 에러 페이지)
            </p>
        </div>
    );
}

export default Error404;