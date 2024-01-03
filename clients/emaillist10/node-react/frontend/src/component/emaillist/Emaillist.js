import React from 'react';
import styles from '../../assets/scss/Emaillist.scss';
import Email from './Email';

function Emaillist({emails, deleteEmail}) {
    return (
        <ul className={styles.Emaillist}>
            {
                emails.map(email => <Email
                                        key={email.no}
                                        no={email.no}
                                        firstName={email.firstName}
                                        lastName={email.lastName}
                                        email={email.email}
                                        deleteEmail={deleteEmail}/>)
            }
        </ul>
    );
}

export default Emaillist;