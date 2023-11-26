import React from 'react';
import {useParams} from "react-router-dom";

import LayoutAccount from "../../layout/LayoutAccount";

export default function Experience() {
    const {accountName} = useParams();

    return (
        <LayoutAccount>
            <div>
                <h1>{accountName}의 Experience 페이지</h1>
            </div>
        </LayoutAccount>
    );
}