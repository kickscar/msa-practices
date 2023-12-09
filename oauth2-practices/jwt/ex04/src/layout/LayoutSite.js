import React from 'react';
import Sidebar from "./Sidebar";

export default function LayoutAccount({children}) {
    return (
        <>
            <div>
                {children}
            </div>
            <Sidebar/>
        </>
    );
}