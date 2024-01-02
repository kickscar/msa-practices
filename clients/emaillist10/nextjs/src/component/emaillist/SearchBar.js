import React from 'react';
import styles from '../../assets/scss/SearchBar.module.scss';

function SearchBar({fetchEmails}) {
    return (
        <div className={styles.Searchbar}>
            <input
                type='text'
                placeholder='찾기'
                onChange={e => {
                    fetchEmails(e.target.value);
                }}/>
        </div>
    );
}

export default SearchBar;