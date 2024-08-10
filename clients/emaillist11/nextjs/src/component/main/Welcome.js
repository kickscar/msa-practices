import React from 'react';
import styles from '../../assets/scss/Welcome.scss';
function Welcome() {
    return (
        <div id={'App'}>
            <h1>Welcome Emaillist</h1>
            <p className={styles.desc}>
                로그인 안 한 경우에만 렌더링 되는 컴포넌트 예제
            </p>
        </div>
    );
}

export default Welcome;