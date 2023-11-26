import React from 'react';
import SidebarAccount from "./SidebarAccount";

export default function LayoutAccount({children}) {
    return (
        <>
            <div>
                {children}
            </div>
            <SidebarAccount/>
        </>
    );
}