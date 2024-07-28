import React from 'react';
import {Email_List} from './assets/scss/Emaillist.scss';
import Email from './Email';

function Emaillist({emails, deleteEmail}) {
    return (
        emails ?
        <ul
            className={Email_List}>
            {
                emails.map(email => <Email
                                        key={email.no}
                                        no={email.no}
                                        firstName={email.firstName}
                                        lastName={email.lastName}
                                        email={email.email}
                                        deleteEmail={deleteEmail} />)
            }
        </ul> :
        null
    );
}

export default Emaillist;