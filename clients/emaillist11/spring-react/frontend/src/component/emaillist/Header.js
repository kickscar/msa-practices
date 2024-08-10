import React from 'react';
import styles from '../../assets/scss/Header.scss'

function Header({name, logout}) {
    return (
        <div className={styles.account}>
            <h1>{`${name}님 안녕하세요.`}</h1>
            <a href='' onClick={(event) => {
                event.preventDefault();
                logout();                
            }}>로그아웃</a>
        </div>
    );
}

export default Header;