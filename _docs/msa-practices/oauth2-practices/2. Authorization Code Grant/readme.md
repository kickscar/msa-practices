## 2. Authorization Code  Grant

### Flow Diagram
![c8e36370cf7eeb49dfb4d5be5ba3fcdc.png](../../../_resources/c8e36370cf7eeb49dfb4d5be5ba3fcdc.png)


### Flow "A"
1. 사용자(Resource Owner)는 브라우저(User-Agent)를 통해 클라이언트(Client)에게 권한을 인가하기 위한 URI(/oauth2/authorize)로 접근한다.

2. 권한을 클라이언트에게 인가하는 동시에 이 인가에 대한 인가 서버(Authorization Server)의 승인(Grant)도 필요하기 때문에 사용자의 권한 인가 클라이언트 엔드포인트는 인가 서버의 인가 엔드포인트(Authorization Endpoint, authorization-uri)로 리다이렉트 된다.

3. 클라이언트가 인가 서버의 인가 엔드포인트로 리다이렉트 할 때, 함께 전달되어야 하는 파라미터는 다음과 같다.
	- 인가 서버에 미리 등록된 클라이언트 고유 아이디(Clinet Identifier, client-id)
	- 인가 서버로 부터의 인가 코드(Authorization Code)를 전달받을 콜백 성격의 리다이렉트 URI

4. 인가 서버의 인가 엔드포인트의 응답(B)는 사용자 인증(Authentication) 화면이다. 쉽게 말해 로그인(sign-in)화면이다. 따라서 사용자도 인가 서버에 인증에 필요한 자격 증명(Credentials) 정보가 미리 등록 되어 있어야 한다. 이것도 쉽게 말해 사용자가 어떤 웹서비스에 username, email 그리고 passwod 등을 제공하는 회원가입(sign-up)이라 이해하면 된다. 


### Flow "A" Impelementation - Authorization Server

#### 1. Keycloak Security Configuration : Introduction
1. \[참고1\] JBoss Keycloak SSO Solution 내용을 반드시 먼저 이해한다.
2. Keycloak 서버 실행
3. Keycloak Security Configuration Objects
	<p>
	아마  A 플로우를 제대로 이해했다면 인가 서버(Authorization Server, Keycloak)에 클라이언트와 사용자를 먼저 등록해야 한다는 것을 눈치챘을 것이다. Keycloak 보안 설정에서 등록이 가능하다. 등록 설정을 하기 전에 Keycloak 보안 설정 객체(대상)들부터 우선 살펴보고 OAuth2 상세의 주요 요소들과의 매핑 관계도 정리해 본다.
	</p>
	
	-	Realm
		<p>
		OAuth2 상세에는 없지만 클라이언트, 사용자, 자격 증명, 역할 및 그룹 집합 등을 관리하는 객체다. Realm들 끼리는 서로 격리되어 있고 각각의 Realm은 자신에 속한 클라이언트, 사용자, 자격 증명, ... 등을 관리한다.
		</p>
	
	-	Client
		<p>
		사용자(자원 소유자, Resource Owner)가 자원에 대한 권한을 인가(Authorization)하고 인가 서버(Keycloak)가 그 인가를 승인(Grant)하는 대상이다. Keycloak에서는 Realm에 클라이언트를 아이디(이름)으로 등록하며 등록과 함께 클라이언트 시크릿(Clinet Secret)을 발급한다. 이 시크릿은 인가 승인 과정에서 사용된다.  
		</p>
	
	- Role
		<p>
		OAuth2 상세에는 권한에 대한 구체적 상세는 없다. 자원 소유자가 자신의 자원에 대한 액세스 권한을 클라이언트에게 인가할 때, 그 액세스 수준을 제한할 필요가 있다. 예를들면 읽기만 허용한다던지 읽기, 쓰기 모두 허용한다던지 하는 경우다. 이를 위해 Keyclock에서 Role을 정의하여 클라이언트가 자원에 대해 수행할 수 있는 작업의 범위를 제한 할 수 있게 한다.
		</p>
	
	-	User
		<p>
		자원 소유자인 사용자(User)는 인가 서버에게 자격 증명(Credentials)을 제출하고 인증(Authentication)되면 Role로 표현된 권한을 클라이언트에게 인가할 수 있다. 인가 서버는 이를 액세스 토큰(Access Token) 발급으로 승인한다.
		</p>

#### 2. Keycloak Security Configuration : Realm 
1. Ream 생성
	![ef87fd813373a21f110a4d92e35bd883.png](../../../_resources/ef87fd813373a21f110a4d92e35bd883.png)
	<br>

	![5d132969b0416fe04fcf2b2ce508113f.png](../../../_resources/5d132969b0416fe04fcf2b2ce508113f.png)
	<pre>
	1) Name 입력
	2) Create 버튼 클릭
	</pre>

2. Realm 생성 완료
	![83b7e0b4645c2152f2d939b02fb0a5bb.png](../../../_resources/83b7e0b4645c2152f2d939b02fb0a5bb.png)
	Token 탭에서 Token의 세부사항을 설정 할 수 있다.

#### 3. Keycloak Security Configuration : Client
1. Client 생성
	![d7a7d610a8c7dc91a283504cb999b9d6.png](../../../_resources/d7a7d610a8c7dc91a283504cb999b9d6.png)

2. Clinet 기본 정보 입력
	![37d4dd0cea6d94375a99de6f0b8fc55d.png](../../../_resources/37d4dd0cea6d94375a99de6f0b8fc55d.png)
	<pre>
	1) client id를 지정한다.
	2) 프로토콜은 openid-connect 이다.
	3) Root URL은 필요 없다.
	</pre>

3. Client 생성 완료
	![d5b742ff3f3692dd1e8a2b95dd1d5df8.png](../../../_resources/d5b742ff3f3692dd1e8a2b95dd1d5df8.png)
		
4. Clinet 세부 설정
	![555536728c703a029ea2d15dda636a7c.png](../../../_resources/555536728c703a029ea2d15dda636a7c.png)	
	<pre>
	 1) Client ID : emaillist
	 2) Enabled : On
	 3) Client Protocol : openid-connect
	 4) Access Type : Confidential
	 5) Standard Flow Enabled : On (Authorization Code Grant)
	 6) Implicit Flow Enabled: On (Implicit Grant)
	 7) Direct Access Grant Enabled : On (Resource Owner Password Credentials Grant)
	 8) Service Account Enabled : On
	 9) Authorization Grant Enabled : On
	10) Valid Redirect URIs: http://localhost:8080/*
	11) Web Origins: *
	12) Backchannel Logout Session Required: On
	</pre>
	
#### 4. Keycloak Security Configuration : Roles
Keyloak에서 Role은 Realm Role과 Client Role 이렇게 두 가지로 구분하여 생성하고 관리할 수 있다. OAuth2는 사용자가 클라이언트에게 권한(Role)을 인가한다 라고 상세하는데 Keycloak 보안 설정에서는 Realm Role 또는 Client Role을 사용자에게 매핑하는 방식으로 이를 구현한다.
	
1. Role 생성
	![7ce00706ca6e99fbd1f16ed8f63f8150.png](../../../_resources/7ce00706ca6e99fbd1f16ed8f63f8150.png)
	Add Role 버튼 클릭
	<br>

	![d805c7a8e24e7bf2a40ac4c5520d6916.png](../../../_resources/d805c7a8e24e7bf2a40ac4c5520d6916.png)
	<pre>
	1) Role Name 등록 (보통 대문자로...)
	2) Save 버튼 클릭
	</pre>

2. 생성 완료
	![334a48a3bf7d71875a83b1553c2466f7.png](../../../_resources/334a48a3bf7d71875a83b1553c2466f7.png)
	Composite Roles를 켜면 여러 복수 role를 합성한 Role을 생성 할 수 있다.
	<br>
	
3. READ Role 생성하기	
	![c5c8742336cc355fca4d4f862a7fefb3.png](../../../_resources/c5c8742336cc355fca4d4f862a7fefb3.png)

#### 5. Keycloak Security Configuration : Users
1. User 생성
	![441fbf70c0ce2ae071ba9ac3744015e7.png](../../../_resources/441fbf70c0ce2ae071ba9ac3744015e7.png)
	Add 버튼 클릭
	<br>

2. 기본 정보 입력
	![0e71b76cacb06918747a1796c0d29e9d.png](../../../_resources/0e71b76cacb06918747a1796c0d29e9d.png)
	<pre>
	1) Username: 신원 증명에 반드시 필요하다.
	2) Email: 입력 하면 신원 증명에 email도 사용할 수 있다.
	3) User Enabled: ON
	4) Save 버튼 클릭
	</pre>

3. 생성 완료
	![9fb00ae9d6a01732dc26b9dbc669ef96.png](../../../_resources/9fb00ae9d6a01732dc26b9dbc669ef96.png)
	생성 후, 반드시 해야하는 설정은 Credentials, Role Mapping 이다.
	<br>

4. Credentials (Password) 설정
	![10698b495d2b6fb4655d66abec4b5e2d.png](../../../_resources/10698b495d2b6fb4655d66abec4b5e2d.png)
	<pre>
	1) Password : 입력
	2) Temporary: OFF
	3) Set Password 버튼 클릭
	</pre>
	
	![cbd8c024f35d20bed08ba97a707225e8.png](../../../_resources/cbd8c024f35d20bed08ba97a707225e8.png)
	Password가 설정된 화면
	<br>
	
5. Role Mapping
	![39d7c474149d1d7d5f453af25d8dcaf1.png](../../../_resources/39d7c474149d1d7d5f453af25d8dcaf1.png)
	<pre>
	1) Client Roles 선택
	2) emaillist 클라이언트 선택하면 등록한 emaillist에 생성한 READ, WRITE Role이 보인다.
	</pre>
	
	![c3f859f914900a108cae5b8884a18520.png](../../../_resources/c3f859f914900a108cae5b8884a18520.png)
	READ role을 Assigne Role에 추가하여 읽기 권한을 부여 했다.
	<br>
	
6. Username 'michol' 사용자 생성 및 Roles (READ, WRITE ) 매핑하기
	![7a7ca6eaaf8d8b3a73c6dde1c0823887.png](../../../_resources/7a7ca6eaaf8d8b3a73c6dde1c0823887.png)
	<br>
	
	![6690d076fe00a0247832eda1dbd05c03.png](../../../_resources/6690d076fe00a0247832eda1dbd05c03.png)
	<br>
	
	![97934f9ca48378a93ac8548c4ab2b970.png](../../../_resources/97934f9ca48378a93ac8548c4ab2b970.png)
	<br>
	
#### 6. Keycloak Security Configuration : OpenID Endpoint Configuration
지금 까지의 Keycloack 설정이 인가 서버 설정의 기본이자 대부분이라 생각한다. 따라서 앞으로도 별 다르게 해줄 설정은 거의 없다. 남은 것은 개발하려고 하는 서비스나 소프트웨어의 보안 요구사항에 맞는 Authorization Grant Type을 선택하고 거기에 맞는 클라이언트를 구현하는 것이다.

지금 까지의 설정을 테스트하기 전에 Keycloak의 Realm Settings으로 가서 다음 링크에서 poscodx2023-realm의 가용한 엔드포인트들을 먼저 확인해 보자.

![0a7ac588eb544d481782d1d857cacc93.png](../../../_resources/0a7ac588eb544d481782d1d857cacc93.png)
OpenID Endpoint Configuration 링크 클릭
	
![87033fff91231c98c9b17f8bfa46e15a.png](../../../_resources/87033fff91231c98c9b17f8bfa46e15a.png)

#### 7. Keycloak Security Configuration :  Issue Access Token
OAuth2의 Authorization Grant Flow의 최종 결과는 인가 서버로 부터의 권한 인가 승인의 증명인 Access Token을 발급 받는 것이다. 그런데 Authorization Code Grant Type의 첫 번째 Flow A만 이해하고 인가 서버의 구현만 끝난 지금은 Authorization Code Grant Flow에 따른 토큰 발급은 불가능하다. 

하지만, 앞의 6. OpenID Endpoint Configuration에서 확인한 Token Endpoint 호출로 인가 서버로 부터 Access Token 발급이 가능하다. 바로 OAuth2의 Authorization Grant Type 중에 Resource Owner Password Credential Grant Type의 플로우는 Token Endpoint 그리고 API 호출이 가능한 API 테스트 툴만 있으면 테스트 진행이 가능하다.

Resource Owner Password Credential Grant에 대한 자세한 설명은 따로 뒤에서 언급하고 지금은 인가 서버 Keycloak 설정에 대한 테스트 목적으로  Resource Owner Password Credential Grant 플로우를 API 테스트 툴(Talend Chrome Plugin)으로 진행해 보자.
	
1. Client 인증
	<p>
	Resource Owner Password Credential Grant 방식에서는 Token Endopint 호출에 사용자 자격 증명을 위해 username과 password를 보낸다. 그리고 Client 인증이 추가적으로 필요하다. Client 인증은  HTTP 기본 인증(Basic Authentication) 방식으로 처리한다. 따라서 Client 인증을 위한 다음의 헤더 내용이 추가된다.
	</p>
	
	```
		
	Authorization Basic ZW1haWxsaXN0OlhRa2x5TVNRNWwyd30sd...
		
	```

	<pre>
	1) Header 이름: "Authorization"
	2) Header 내용: "Basic" + " " + Base64(ClientName + ":" + Clinet Secret)
	</pre>
	
	Client Secret 확인
	![9c0194852a9bf06ea35d0f5a98d537cf.png](../../../_resources/9c0194852a9bf06ea35d0f5a98d537cf.png)
	<br>
	
	Basic Auhtentication Header 설정(Talend Chrome Plugin)
	![24c95621d6a0f526b1963d9ce3591113.png](../../../_resources/24c95621d6a0f526b1963d9ce3591113.png)
			
	![0137ff91e78d035910a50c633ea77b43.png](../../../_resources/0137ff91e78d035910a50c633ea77b43.png)
		
2. 사용자 인증
	<p>
	사용자 인증에 필요한 Credential은 POST 방식으로 HTTP 바디(HTTP Form Data)에 다음과 같은 내용이 포함되어 전달된다.
	</p>
	
	<pre>
	1) grant_type : password
	2) username : username or email
	3) password
	</pre>
	
3. Keycloak EndPoint
	<p>
	http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	</p>
	
4. 테스트
	![7b239e79c6073dbb3af7dd5c27dbbd15.png](../../../_resources/7b239e79c6073dbb3af7dd5c27dbbd15.png)
	<br>
	
5. jwt.io 에서 Access Token 확인하기
	![70df91da63bbf37c14290d4f739ffdec.png](../../../_resources/70df91da63bbf37c14290d4f739ffdec.png)


#### 8. Keycloak Security Configuration :  Refresh Access Token via Refresh Token
Resource 접근과 함께 보내는 Access Token의 유효 기간이 만료되거나 클라이언트 애플리케이션의 Token 저장 방식에 따라 휘발된 경우에는 Fresh Token으로 Access Token을 재발급 받을 수 있다.
	
1. Client 인증
	<p>
	앞의 Access Token을 발급 받았을 때와 마찬가지로 클라이언트 인증을 위한 Authorization Header가 세팅되어야 한다.
	</p>

2. 사용자 인증
	<p>
	Access Token을 재발급 받기 위해 사용자의 Credential은 필요하지 않다. Refresh Token을 사용하기 때문이다. 따라서 grant_type이 password에서 refresh_token으로만 변경한다. post 방식으로 보내야 하는 form data는 다음과 같다
	</p>
	
	- grant_type: refresh_token
	- refresh_token: eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI.......

3. Keycloak EndPoint
	<p>
	http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	</p>
	
4. 테스트
	![29584e432897108e3841dc5cd3792952.png](../../../_resources/29584e432897108e3841dc5cd3792952.png)


### Flow "A" Impelementation - Client

#### 1. emaillist10 : Introduction
1. \[참고2\] Spring Security Basics 내용을 반드시 먼저 이해한다.
2. 예제: /clients/emaillist10/backend
3. Spring Boot Security & OAuth Starter
	<p>
	spring security oauth2 client를 사용하면 비교적 쉽게 Authorization Code Grant 플로우를 지원하는 Clinet를 작성할 수 있다.
	</p>
	
	```xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-security</artifactId>
	</dependency>
	
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-oauth2-client</artifactId>
 	</dependency>
	```

#### 2. emaillist10 : Spring Security Configuration
OAuth2 Client 설정에 사용하는 HttpSecurity Security Builder DSL은 oauth2Login()와 oauth2Client() 이다. oauth2Client()는 OAuth2의 액세스 모델인 인가 승인 그리고 위임만 사용할 경우 고려해 볼 수 있다. 사실, OAuth2는 인증(Authetication) 프로토콜이 아니다.  인가(Authorization)에만 관심있는 프로토콜이다. 

OAuth2의 인가 모델 위에 자연스럽게 인증 모델이 통합된 것이 OpenID Connect 프로토콜이다. oauth2Client()는 OAuth2만 지원하기 때문에 Access Token 발급까지만 가능한  반면, oauth2Login()은 OIDC까지 지원하기 때문에 ID Token 발급도 가능하며 이를 통해 사용자 Identity까지 다룰 수 있다. 

1. 설정 내용: com.poscodx.emaillist.security.Config.java
	
	![5632d824c4adc1cf61f8f3e18042be3c.png](../../../_resources/5632d824c4adc1cf61f8f3e18042be3c.png)
	
	- (1) CSRF를 비활성 시킨다.
	- (2) session filter를 비활성 시켜 세션을 사용하지 않는다.
	- (3) 접근 제어를 하지 않는다.
	- (4) 사용자가 클라이언트를 인가하는 Base URL
	  <p>
	  예제의 인가 URI 형식은 /oauth2/authorize/{registrationId} 이다. registrationId는 다음 장의 Client Registration에서 설정한다.
	  </p>
		
   - (5) Authorization Code 반환 리다이렉트 Base URI
     <p>
	 인가 서버에 등록된 Client에 권한 인가를 하는 사용자가 인가 서버로 인증이 성공하면 클라이언트의 Authorization Code 반환 URI로 리다이렉트 하면서 Authorization Code를 반환한다. 이 URI의 Base URI를 설정한다. 반드시 '/login' 으로 시작해야 하며 이 설정에서는 '/login/oauth2/code/.... '가 된다. 정확한 URI 설정은 다음 장의 Client Registration에서 할 수 있다. 이 URL에 매핑된 핸들러는 전달 받은 Authorization Code로 인가 서버와 남은 인가 승인 플로우를 진행하게 된다.
	</p>
   
   - (6) SuccessHandler 등록
     <p>
     인가 승인 플로우의 마지막은 인가 서버로 부터의 토큰 발급이다. 클라이언트에 등록된 SuccessHandler에서 그 토큰을 처리 할 수 있다.
    </p>
	
   -  성공 URI 리다이렉션
      <p>
      인가 승인 플로우 마지막을 SuccessHandler 대신 특정 url로 리다이렉트 할 수 있다. 예제에서는 주석 처리 하였다. 지금 구현하고 있는 OAuth2 클라이언트와 후반부에서 구현 될 자원 접근 API 호출 및 View를 담당하는 JS로 작성된 클라이언 와의 토큰 전달 플로우 때문에 주석 처리했다. 이 것도 뒤에서 다시 설명한다.
	 </p>

2. Security Filters
	<pre>
	01. DisableEncodeUrlFilter                  (default)   1
	02. SecurityContextPersistenceFilter        (default)   2
	03. WebAsyncManagerIntegrationFilter        (default)   3
	04. HeaderWriterFilter                      (default)   4
    05. OAuth2AuthorizationRequestRedirectFilter            5*
    06. OAuth2LoginAuthenticationFilter                     6*
	07. DefaultLogoutPageGeneratingFilter                   7      
	08. RequestCacheAwareFilter                 (default)   8
	09. SecurityContextHolderAwareRequestFilter (default)   9
	10. AnonymousAuthenticationFilter           (default)  10
	11. SessionManagementFilter                 (default)  11
	12. ExceptionTranslationFilter              (default)  12
	13. AuthorizationFilter                                13
	</pre>

#### 3. emaillist10 : Client Registration & Grant Provider Configuration
사용자는 emaillist10 클라이언트에게 권한 인가를 하기 위해 emaillist10의 인가 엔드포인트(Authorization Endpoint)에 접근한다. 이 접근 자체가 사용자가 클라이언트를 인가를 목적으로 하지만 인가 서버로의 사용자 인증으로 최종 인가 승인이 된다. emaillist10는 사용자의 인가 접근을 인가 서버의 인가 승인 엔드포인트로 리다이렉트 한다.

이를 위해 인가 서버(Grant Provider)를 emaillist10에 설정해야 한다. 또 인가 서버에게 유효한 클라인언트 임을 입증하기 위해 인가 서버에 등록된 클라이언트 정보를 emaillist10에 설정해야 한다. application.yml 에 그 설정 내용을 확인해 보자.

```xml

	spring:
		security:
			oauth2:
				client:
					provider:
						keycloak-authorization-server:
							issuer-uri: http://localhost:5555/realms/poscodx2023-realm
							authorization-uri: http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/auth
							token-uri: http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
					registration:
						emaillist-oauth2-client:
							provider: keycloak-authorization-server
							authorization-grant-type: authorization_code
							redirect-uri: "http://localhost:8080/login/oauth2/code/{registrationId}"
							client-id: emaillist
							client-secret: XQklyMSQ5l2wmKK8wuoa0aV5Os5M2fY1
							scope: [openid, profile, email, roles]

```

Provider ID,  'keycloak-authorization-server' 그리고 Registration ID, 'emaillist-oauth2-client'는 각각 임의로 네이밍해도 상관 없다. 단, registration.emaillist-oauth2-client.provider 에는 당연히 네이밍한 Provider ID를 설정해야 한다.

1. Grant Provider
	<p>
	인가 서버 Keycloak 설정에서 확인한 Endpoint들 중에 다음 몇 가지를 필수로 설정한다.
	</p>
	
	- issuer-uri
		<p>
      	인가 서버 Endpoint들의 Base URI이다. 접근 했을 때 반환하는 내용은 발급한 토큰(JWT)의 iss cliam이 있는 경우, 이 클레임의 유효성을 검증한다. 만약 접근 오류가 나면 클라이언트가 시작할 때 예외가 발생할 수 있다.
		</p>
		
	- authorization-uri
		<p>
      	인가 승인 Endpoint 이다. 사용자가 클라이언트의 승인 Endpoint로 접근할 때 클라이언트가 리다이렉트 하는 인가 서버의 인가 승인 Endpoint 이다.
		</p>
		
	- token-uri
		<p>
      	인가 서버의 토큰 발급 Endpoint 이다. 플로우 "A" 에서는 접근하지 않지만 뒤의 플로우에서 토큰을 발급 받기 위해 접근한다. 
		</p>

2. Client Registration
	- provider
	- authorization-grant-type
	- redirect-uri
	- client-id
	- client-secret
 	- scope


### Flow "A" Impelementation - Test

#### 1. Client Authorization Endpoint
사용자(Resource Owner)는 브라우저(User Agent) 주소창에 Client(emaillist10)의 인가 엔드포인트(Authorization Endpoint)에 접근한다.

![01d3e08d9f247a15703c75e0e33b3366.png](../../../_resources/01d3e08d9f247a15703c75e0e33b3366.png)

Client emaillist10에 설정된 /oauth2/authorize/{registrationId} URI를 감시하는 Spring Security Filter는 OAuth2AuthorizationRequestRedirectFilter 이다.

1. OAuth2AuthorizationRequestRedirectFilter#doFilterInternal 
	![e19b89f2678d17b466c18ab6fb89970d.png](../../../_resources/e19b89f2678d17b466c18ab6fb89970d.png)
	Endpoint(/oauth2/authorize/emaillist-oauth2-client)로 받은 클라이언트에 대한 사용자 인가 요청(HttpServletRequest)을 인가 서버의 Endpoint(/realms/poscodx2023-realm/protocol/openid-connect/auth)로 리다이렉트 하는 요청(OAuth2AuthorizationRequest)으로 리졸브 하고 리다이렉트 한다. 

2.  DefaultOAuth2AuthorizationRequestResolver#resolve#1
	![4b2c60f82a4fa4ae7767b86d7f677afc.png](../../../_resources/4b2c60f82a4fa4ae7767b86d7f677afc.png)
	첫  번째  오버로드 메서드 resolve 에서는 요청 URI 에서 registerationId를 추출하는 것을 알 수 있다. 파라미터 'action'를 전달하지 않기 때문에 redirectUriAction은 'login' 이다.
	
3. DefaultOAuth2AuthorizationRequestResolver#resolve#2
	![277825c767b97e6e24f0406a6ec839ed.png](../../../_resources/277825c767b97e6e24f0406a6ec839ed.png)
	
	앞의 Client Registration Configuration 에서 인가 서버에 등록된 클라이언트 정보를 application.yml에 설정하는 것을 살펴 보았다. emaillist10 클라이언트가 실행되면 이 정보를 관리하는 InMemoryClientRegistrationRepository Bean이 Application Context에 생성된다. 두 번째  오버로드 메서드 resolve 에서는 주입받은 InMemoryClientRegistrationRepository Bean을 사용하여 registerationId으로 Client 정보를 담고 있는 객체 ClientRegistration를 찾는다. 찾게 되면, ClientRegistration를 참고하여 인가 서버로 리다이렉트 할 URI를 포함하여 함께 넘겨야 하는 파라미터 정보들을 담을 OAuth2AuthorizationRequest 객체를 빌드하게 된다. 	

4.  OAuth2AuthorizationRequestRedirectFilter#sendRedirectForAuthorization 
	 ![2876558d9e7a9d8d67ce3d36dbb7946c.png](../../../_resources/2876558d9e7a9d8d67ce3d36dbb7946c.png)
	 
	 인가 서버로의 리다이렉트 정보를 담은 OAuth2AuthorizationRequest 객체를 전달 받아 리다이렉트 한다. 한편 코드를 보면 리다이렉트 전에 인가 승인 타입(Authorization Grant Type)이 Authorization Code 타입이면 HttpSessionOAuth2AuthorizationRequestRepository가 인가 요청를 저장하고 이 후에 인가 승인 결과인 토큰을 Session에 저장하는데 예제 클라이언트 emaillist10은 Stateless로 설정하여 둘다 따로 저장하지 않는다.
	 
#### 2. Authorization Server Authorization Endpoint
사용자(Resource Owner)는 브라우저(User Agent) 주소창에 Client(emaillist10)의 인가 엔드포인트(Authorization Endpoint)에 접근하면 Client는 인가 승인을 위한 정보를 파라미터로 인가 서버(Keycloak)의 Authorization Endpoint로 리다이렉트를 한다. 이 Endpoint의 응답은 사용자 인증(Authentication)을 위한 입력폼이다.
![4b2cb058d7db346c78340fe9c8063bec.png](../../../_resources/4b2cb058d7db346c78340fe9c8063bec.png)

리다이렉트 된 Endpoint URI에 인가 서버로 전달하는 파라미터를 보면,
	
	1. response_type
	2. client_id
	3. scope
	4. redirect_uri

앞의  application.yml에 설정한 Client Registration 내용임을 알 수 있다.


### Flow "B"
인가 서버(Keycloak)의 Authorization Endpoint의 응답 화면 즉, 사용자 인증(Authentication)을 위한 입력폼에 사용자가 인증을 하는 플로우이다. 사용자는 자신의 자격 증명(Credentials)을 인가 서버에 제출해야 한다. 

인증이 되었다는 것은 인증된 사용자(Resource Owner)가 이 플로우를 진행하고 있는 클라인언트에게 자신의 자원에 접근할 수 있는 권한을 인가(Authorization) 하겠다는 의미이며 인가 서버는 인가를 클라이언트에게 승인(Grant) 해준다. 그 증거로 토큰을 발급하지만 Authorization Code Grant Flow 에서는 인가 코드(Athorization Code)를 현재 브라우저 주소창의 URI(인가 서버의 인가 Endpoint )의 파라미터로 보낸 리다이렉트 주소로 되돌려 준다.  

### Flow "C", "D", "E"
클라이언트의 OAuth2LoginAuthenticationFilter는 인가 승인 코드(Authorization Code)를 받는 클라이언트 URI(/login/oauth2/code/emaillist-oauth2-client)를 감시한다. 이 필터에서 URI와 함께 전달된 인가 코드를 가지고 Access Token를 발급받기 위한 flow(C, D, E)를 진행한다.
	

  