import React from 'react';
import { useOutletContext } from 'react-router';
import styles from '../../assets/scss/Email.scss';

function Email({no, firstName, lastName, email, deleteEmail}) {
    const { roles } = useOutletContext();

    return (
        <li className={styles.Email}>
            <h4>{firstName} {lastName}</h4>
            <span>{email}</span>
            {
            roles.includes("WRITE") ? 
                <a onClick={(event) => {
                    event.preventDefault();
                    deleteEmail(no);
                }}/> :
                null
            }
        </li>
    );
}

export default Email;