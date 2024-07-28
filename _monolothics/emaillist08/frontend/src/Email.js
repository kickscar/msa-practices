import React from 'react';
import {_Email} from './assets/scss/Email.scss';

function Email({no, firstName, lastName, email, deleteEmail}) {
    return (
        <li className={_Email}>
            <h4>{firstName}{lastName}</h4>
            <span>{email}</span>
            <a
                href=''
                onClick={e => {
                    e.preventDefault();
                    deleteEmail(no);
                }}></a>
        </li>
    );
}

export default Email;