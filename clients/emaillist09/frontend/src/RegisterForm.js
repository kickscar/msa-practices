import React, {useRef} from 'react';
import styles from './assets/scss/RegisterForm.scss';

function RegisterForm({addEmail}) {
    const refForm = useRef(null);

    return (
        <form
            ref={refForm}
            className={styles.RegisterForm}
            onSubmit={(e) => {
                e.preventDefault();
                
                const email = {
                    firstName: e.target.firstName.value,
                    lastName: e.target.lastName.value,
                    email: e.target.email.value
                };

                addEmail(email);
                refForm.current.reset();
            }}>
            <input type='text' name='firstName' placeholder='성' className={styles.InputFirstName} />
            <input type='text' name='lastName' placeholder='이름' className={styles.InputLastName} />
            <input type='text' name='email' placeholder='이메일' className={styles.InputEmail} />
            <input type='submit' value='등록' />
        </form> 
    );
}

export default RegisterForm;