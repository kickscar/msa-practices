## JWT Client : React.js

#### Store JWT Locally
1. access token과 refresh token을 함께 저장한다.
2. access token은 client application의 메모리에 저장한다. (react context's state)
3. access token은 휘발된다.(새로고침 또는 브라우저를 닫으면 사라진다.)
4. access token이 휘발되면 refresh token으로 access token을 재발급 받는다.
5. refresh token은 휘발되지 않게 하기 위해 cookie에 저장하되 httpOnly 옵션으로 구워 JS가 접근하지 못하게 한다.

#### React Context
1. access token의 payload에 보통 애플리케이션에서 필요한 기본적인 사용자 정보(no, username, profile 등)를 담는다.

2. component tree의 최상위 component가 access token을 발급받고 이를 하위 component들에 전달하는 방법으로 전역(global) 접근이 가능한 react context를 사용

3.  Component &lt;AuthContext&gt; : ex03
	1) 
 