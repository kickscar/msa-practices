import React from 'react';
import ReactDOM from 'react-dom/client';
import {App} from './App.js';

//
// console.* 함수들 production 모드일 때 disable 시킴
// package.json 실행 스크립트에 '--mode production' 로 process.env.NODE_ENV 세팅 시킴
// replace console.* for disable log on production mode
//
if(process.env.NODE_ENV === 'production') {
  console.log = () => {};
  console.error = () => {};
  console.debug = () => {};
  console.info = () => {};     
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(<App />);