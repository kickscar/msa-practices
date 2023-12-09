## 2. Authorization Code  Grant

#### Flow Diagram
![c8e36370cf7eeb49dfb4d5be5ba3fcdc.png](../../_resources/c8e36370cf7eeb49dfb4d5be5ba3fcdc.png)

#### A-1: 플로우 설명
1. 사용자(Resource Owner)는 브라우저(User-Agent)를 통해 클라이언트(Client)에게 권한 인가를 하기 위해 URI(/oauth2/authorize)로 접근한다.

2. 클라이언트에게 주는 권한 인가와 동시에 인가 서버(Authorization Server)의 이 인가에 대한 승인(Grant)도 필요하기 때문에 클라이언트는 사용자의 권한 인가 URI 접근을 인가 서버의 인가 엔드포인트(Authorization Endpoint, authorization-uri)로 리다이렉트 할 필요가 있다.

3. 클라이언트가 인가 서버의 인가 엔드포인트로 리다이렉트 할 때, 인가 엔드포인트로 전달되는 파라미터는 다음과 같다.
	- 인가 서버에 미리 등록된 클라이언트 고유 아이디(Clinet Identifier, client-id)
	- 인가 서버로 부터의 인가 코드(Authorization Code)를 전달받을 콜백 성격의 리다이렉트 URI

4. 인가 서버의 인가 엔드포인트의 응답(B)는 사용자 인증(Authentication) 화면이다. 쉽게 말해 로그인(sign-in)화면이다. 따라서 사용자도 인가 서버에 인증에 필요한 자격 증명(Credential) 정보가 미리 등록 되어 있어야 한다. 이것도 쉽게 말해 사용자가 어떤 웹서비스에 username, email 그리고 passwod 등을 제공하는 회원가입(sign-up)이라 이해하면 된다. 

#### A-2: 플로우 구현 - 인가 서버 
1. \[참고1\] JBoss Keycloak SSO Soultion 내용을 반드시 먼저 이해한다.
2. Keycloak 서버 실행
3. Keycloak Security Configuration Objects
	<p>
	아마 이 플로우를 제대로 이해했다면 인가 서버(Authorization Server, Keycloak)에 클라이언트와 사용자를 먼저 등록해야 한다는 것을 눈치챘을 것이다. 등록은 Keycloak 보안 설정에서 가능하다. 등록 설정을 하기 전에 Keycloak 보안 설정 객체(대상)들부터 우선 살펴보고 OAuth2 상세의 주요 요소들과의 매핑 관계도 생각해 보자.
	</p>
	
	-	realm
		<p>
		사용자, 자격 증명, 역할 및 그룹 집합 등을 관리하는 객체다. 사용자는 realm에 속하고 자격 증명(Credentials)을 통해 인증 받는다. realm들은 서로 격리되어 있고 각각의 realm은 자신에 속한 사용자만 관리하고 인증한다.
		</p>
	
	-	client
		<p>
		자원 소유자(Resource Owner)가 자원에 대한 권한을 인가하고 인가 서버(Keycloak)가 그 인가를 승인해야 하는 대상이다. Keycloak에서는 realm에 클라이언트를 아이디(이름)으로 등록하며 등록과 함께 Clinet Secret를 발급한다. 시크릿은 이 후 플로우에서 사용된다.  
		</p>
	
	- role
		<p>
		자원 소유자는 사용자와 클라이언트에 대한 권한과 액세스 수준을 정의하고 관리하는 데 role를 사용할 수 있다. role은 사용자나 클라이언트가 자원에 대해 수행할 수 있는 작업의 범위를 지정하는 데 사용된다.
		</p>
	
	-	user
		<p>
		사용자는 자격 증명을 통해 인증(Authentication)되면 role로 표현된 권한을 클라이언트에게 인가하며 인가 서버(Keyclock)은 이를 액세스 토큰 발급으로 승인한다.
	</p>
	
4. Keycloak Security Configuration : realm
	-	Ream 생성
		![ef87fd813373a21f110a4d92e35bd883.png](../../_resources/ef87fd813373a21f110a4d92e35bd883.png)
		<br>
	
		![5d132969b0416fe04fcf2b2ce508113f.png](../../_resources/5d132969b0416fe04fcf2b2ce508113f.png)
		1)	Name 입력
		2)	Create 버튼 클릭
		<br>

	-	생성 완료
		![83b7e0b4645c2152f2d939b02fb0a5bb.png](../../_resources/83b7e0b4645c2152f2d939b02fb0a5bb.png)
		Token 탭에서 Token의 세부사항을 설정 할 수 있다.
		<br>

5. Keycloak Security Configuration : clients
	-	Client 생성
		![d7a7d610a8c7dc91a283504cb999b9d6.png](../../_resources/d7a7d610a8c7dc91a283504cb999b9d6.png)
		Create 버튼 클릭
		<br>
	
		![37d4dd0cea6d94375a99de6f0b8fc55d.png](../../_resources/37d4dd0cea6d94375a99de6f0b8fc55d.png)
		1)	client id를 지정한다.
		2)	프로토콜은 openid-connect 이다.
		3)	Root URL은 필요 없다.
		<br>

		![d5b742ff3f3692dd1e8a2b95dd1d5df8.png](../../_resources/d5b742ff3f3692dd1e8a2b95dd1d5df8.png)
		<br>
	
	-	Clinet 설정
		![5265491b77e6082117669d6a7e2af3d1.png](../../_resources/5265491b77e6082117669d6a7e2af3d1.png)
		1)	Enabled : ON
		2)	Client Protocol : openid-connect
		3)	Access Type : Confidential
		4)	Standard Flow Enabled : On (Authorization Code Grant O)
		5)	Direct Access Grant Enabled : On (Resource Owner Password Credentials Grant )
		6)	Service Account Enabled : On
		7)	Authorization Grant Enabled : On

6. Keycloak Security Configuration : roles
	Keyloak에서는 Role로 표현되는 권한을 Realm Role과 Client Role 이렇게 두 가지로 나눠 생성할 수 있다. OAuth2 에서는 사용자가 클라이언트에게 권한(Role)을 인가한다. Keycloak 설정에서
	-	Role 생성
		![7ce00706ca6e99fbd1f16ed8f63f8150.png](../../_resources/7ce00706ca6e99fbd1f16ed8f63f8150.png)
		Add Role 버튼 클릭
		<br>

		![d805c7a8e24e7bf2a40ac4c5520d6916.png](../../_resources/d805c7a8e24e7bf2a40ac4c5520d6916.png)
		1)	Role Name 등록 (보통 대문자로...)
		2)	Save 버튼 클릭
		<br>

		![334a48a3bf7d71875a83b1553c2466f7.png](../../_resources/334a48a3bf7d71875a83b1553c2466f7.png)
		1)	생성 완료
		2)	Composite Roles를 켜면 여러 복수 role를 합성한 Role을 생성 할 수 있다.
		<br>
	
		![c5c8742336cc355fca4d4f862a7fefb3.png](../../_resources/c5c8742336cc355fca4d4f862a7fefb3.png)
		같은 방식으로 READ role도 생성 한다.
		<br>

7. Keycloak Security Configuration : users
	-	User 생성
		![441fbf70c0ce2ae071ba9ac3744015e7.png](../../_resources/441fbf70c0ce2ae071ba9ac3744015e7.png)
		Add 버튼 클릭
		<br>
	
		![0e71b76cacb06918747a1796c0d29e9d.png](../../_resources/0e71b76cacb06918747a1796c0d29e9d.png)
		1)	Username: 신원 증명에 반드시 필요하다.
		2)	Email: 입력시, 신원 증명에 email 주소도 사용할 수 있다.
		3)	User Enabled: ON
		4)	Save 버튼 클릭
		<br>
	
		![9fb00ae9d6a01732dc26b9dbc669ef96.png](../../_resources/9fb00ae9d6a01732dc26b9dbc669ef96.png)
		1)	생성 완료
		2)	생성 후, 해야하는 중요한 설정은 Credentials, Role Mapping 이다.
		<br>

	-	Credentials (password) 설정
		![10698b495d2b6fb4655d66abec4b5e2d.png](../../_resources/10698b495d2b6fb4655d66abec4b5e2d.png)
		1)	Password : 입력
		2)	Temporary: OFF
		3)	Set Password 버튼 클릭
		<br>
	
		![cbd8c024f35d20bed08ba97a707225e8.png](../../_resources/cbd8c024f35d20bed08ba97a707225e8.png)

	-	Role Mapping
		![39d7c474149d1d7d5f453af25d8dcaf1.png](../../_resources/39d7c474149d1d7d5f453af25d8dcaf1.png)
		1)	Client Roles 선택
		2)	emaillist 클라이언트 선택하면 등록한 emaillist에 생성한 READ, WRITE role이 보인다.
		<br>
	
		![c3f859f914900a108cae5b8884a18520.png](../../_resources/c3f859f914900a108cae5b8884a18520.png)
		READ role을 Assigne Role에 추가하여 읽기 권한을 부여 했다.

	-	Username 'michol' 사용자 생성(READ, WRITE Roles)
		![7a7ca6eaaf8d8b3a73c6dde1c0823887.png](../../_resources/7a7ca6eaaf8d8b3a73c6dde1c0823887.png)
		<br>
	
		![6690d076fe00a0247832eda1dbd05c03.png](../../_resources/6690d076fe00a0247832eda1dbd05c03.png)
		<br>
	
		![97934f9ca48378a93ac8548c4ab2b970.png](../../_resources/97934f9ca48378a93ac8548c4ab2b970.png)
		<br>
	
8. OpenID Endpoint Configuration
	<p>
	Test를 하기전에 Keycloak의 Realm Settings의 다음 링크에서 poscodx2023-realm의 가용한 엔드포인트들을 먼저 확인해 보자.
	</p>
	
	![0a7ac588eb544d481782d1d857cacc93.png](../../_resources/0a7ac588eb544d481782d1d857cacc93.png)
	OpenID Endpoint Configuration 링크 클릭!
	<br>
	
	![87033fff91231c98c9b17f8bfa46e15a.png](../../_resources/87033fff91231c98c9b17f8bfa46e15a.png)

9. Issue Access Token
	<p>
	지금 까지의 Keycloack 설정이 인가 서버 설정의 대부분으로 앞으로도 별다르게 해 줄 설정은 거의 없다. 남은 것은 각각의 Authorization Grant Type Flow에 맞게 클라이언트를 구현해 주면 된다. 우선, Keycloack 설정 테스트를 위해 비교적 쉽게 Token Endopint 호출로만 Access Token 발급이 가능한 Resource Owner Password Credential Grant 방식으로 테스트 해보자.
	</p>
	
	-	Client 인증
		<p>
		Resource Owner Password Credential Grant 방식에서는  Token Endopint 호출에 사용자 자격 증명을 위해 username과 password를 보낸다. 그리고 Client 인증이 추가적으로 필요하다. Client 인증은  HTTP 기본 인증(Basic Authentication) 방식으로 처리한다. 따라서 Client 인증을 위한 헤더 내용은 다음과 같다.
		</p>
	
		```
		Authorization Basic ZW1haWxsaXN0OlhRa2x5TVNRNWwyd30sd...
		
		```
	
		1)	Header 이름: "Authorization"
		2) Header 내용: "Basic" + " " + Base64(ClientName + ":" + Clinet Secret)
		3) Client Secret 확인
			![9c0194852a9bf06ea35d0f5a98d537cf.png](../../_resources/9c0194852a9bf06ea35d0f5a98d537cf.png)
		4)	Basic Auhtentication Header 설정(Talend Chrome Plugin)
			![24c95621d6a0f526b1963d9ce3591113.png](../../_resources/24c95621d6a0f526b1963d9ce3591113.png)
			
			![0137ff91e78d035910a50c633ea77b43.png](../../_resources/0137ff91e78d035910a50c633ea77b43.png)
		
	-	사용자 인증
		<p>
		사용자 인증에 필요한 Credential은 POST 방식으로 HTTP 바디(HTTP Form Data)에 포함되어 전달된다.
		</p>
		
		1)	grant_type : password
		2)	username : username or email
		3)	password

	-	Keycloak EndPoint
		http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	
	-	테스트
		![7b239e79c6073dbb3af7dd5c27dbbd15.png](../../_resources/7b239e79c6073dbb3af7dd5c27dbbd15.png)

	-	jwt.io 에서 Access Token 확인하기
		![70df91da63bbf37c14290d4f739ffdec.png](../../_resources/70df91da63bbf37c14290d4f739ffdec.png)

10.	Refresh Access Token via Refresh Token
	<p>
	Resource Server의 인가를 목적으로 보내는 Access Token 이 유효 기간이 만료되거나 클라이언트 애플리케이션의 Token 저장 방식에 따라 휘발된 경우 Fresh Token으로 Access Token을 재발급 받아 리소스 접근 인가를 위해 사용할 수 있다.
	</P>
	
	- Client 인증
		앞의 Access Token을 발급 받았을 때와 마찬가지로 클라이언트 인증을 위한 Authorization Header가 세팅되어야 한다.
	
	- 사용자 인증
		<p>
		Access Token을 재발급 받기 위해 사용자의 Credential은 필요하지 않다. Refresh Token을 사용하기 때문이다. 따라서 grant_type이 password에서 fresh_token으로만 변경하면 된다. post 방식으로 보내야 하는 form data는 다음과 같다
		</p>
	
		1)	grant_type: fresh_token
		2)	refresh_token: eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...

	-	Keycloak EndPoint
		http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	
	-	테스트
		![29584e432897108e3841dc5cd3792952.png](../../_resources/29584e432897108e3841dc5cd3792952.png)










#### A-3: 플로우 구현 - 클라이언트 







5. Redirection URI
	- 로그인 화면(B)에서 사용자가 인증(로그인)한 것은 1번의 URL에 포함된 클라이언트에게 권한 인가를 하겠다는 의미다.
	- 인가 서버는 사용자 인증이 성공하고 4번에서 보내온 Client Id가 미리 인가 서버에 등록된 클라이언트라면 사용자의 권한 인가를 승인하게 된다. 
	- Authorization Code  Grant 플로우에서는 바로 승인의 증거인 액세스 토큰을 
	

  