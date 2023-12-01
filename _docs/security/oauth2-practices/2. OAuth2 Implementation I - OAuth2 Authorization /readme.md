## 2. OAuth2 Implementation I - OAuth2 Authorization Server

#### 1. [Spring Security Project](https://spring.io/projects/spring-security)
Spring Security 프레임워크 기반으로 OAuth2 Authorization Server 및 OAuth2 Client 그리고 OAuth2  Resource Server 구현이 가능하였다. 하지만 2022년 6월, Spring Security Project는 '[ㅈ3ㅁ 4ㄷㅌ,./.]ㅔ88ㅑㅑClient 및 Resource Server 지원만 남기고 Authorization Server는 Spring Authorization Server 라는 이름의 단독 프로젝트로 이관 시켰다.

#### 2. [Spring Authorization Server](https://spring.io/projects/spring-authorization-server)
OAuth 2.1 스펙과 OpenID Connect 1.0 (OIDC) 스펙을 완벽히 지원하는 SSO(Single Sign-On) OAuth2 Authorization Server 작성을 가능하게 해주는 프레임워크이다. 

![a79779d9a01cded43757c865f9b7c232.png](../../../_resources/a79779d9a01cded43757c865f9b7c232.png)

#### 3. Keycloak
1. Keycloak SSO Soultion
	<p>
	<a href='https://www.keycloak.org'>Keycloak</a>은 JBoss 팀이 개발한 Java 기반의  OAuth 2.1, OIDC 1.0 그리고 SAML를 지원하는 SSO 솔류션이다. 2014년, RedHat이 JBoss를 인수하면서 RH-Keycloak 이라는 이름의 상용 솔류션을 출시했다. 한편, RedHat 내에 WildFly로 이름을 바꾼 JBoss 팀이 지속으로 Keycloak 오픈소스 프로젝트를 진행하고 있으며 현재(2023, 10월) 22.0.5 LTS 버젼까지 출시되어 있는 상태다.
	</p>

2. Keycloak Embeded
	<p>
	자바 기반의 오픈 소스 솔류션이기 때문에 다양한 방식으로 보안 인프라에 운용된다. 완성도가 높은 솔류션이기 때문에 별다른 커스터마이징 또는 부가적인 코딩작업 없이 설치만으로 운용이 가능하며 클라우드 환경의 도커 컨테이너에서의 운용도 많이 선호된다.
	</p>
	<p>
	한편, Spring Cloud 기반 MSA 인프라에서는 Spring Boot에 임베드하는 방식도 많이 선택된다. 자바 기반이라 소스 레벨에서 Spring Boot에 임베드하기가 어렵지 않으며 커스터마이징이 가능하다는 것도 장점이다. 하지만, 무엇보다 Keyclock 자체를 Spring Cloud 기반의 MSA 서비스로 등록하여 고가용성을 확보할 수 있다는 것이 MSA 인프라 환경에서는 더 큰 매력이라 볼 수 있다.
 	</p>

#### 4. Keycloak Embeded
1. 예제 소스: /servers/embedded-springboot-keycloak-server/embedded-keycloak-server
2. 실행 환경
	- Keycloak 18.0.0
	- Java 11
	- Spring Boot 2.6.8
	- MariaDB 10.x
3. 주요 설정(application.yml)
	-	datasource
		
		```yaml
		datasource:
			driver-class-name: org.mariadb.jdbc.Driver
			url: jdbc:mariadb://localhost:3306/keycloak?characterEncoding=utf8
			username: keycloak
			password: keycloak
		```
	
	- server
		
		```yml
		server:
		  forward-headers-strategy: native
		  port: 5555
		  servlet:
		  context-path: "/"
		```
	
	-	keycloak
	
		```yml
		custom:
			server:
				keycloak-path: ""
				adminUser:
					username: admin
					password: admin
					create-admin-user-enabled: true
		```

4. 빌드
	```
	$ mvn clean install
	```

5. 실행
	```sh
	$ java -jar embedded-keycloak-server.jar
	```

6. 접근 (http://localhost:5555)
	
	![98087a8d3795b7f5c5d6099e9274f77b.png](../../../_resources/98087a8d3795b7f5c5d6099e9274f77b.png)
	
8. 관리자 로그인

	![c19ac627b9a13749a1fe5b7c98b9e1b6.png](../../../_resources/c19ac627b9a13749a1fe5b7c98b9e1b6.png)

#### 5. Keycloak Security Configuration Objects
Authorization Server가 인증을 수행하고 클라이언트의 접근 권한 확인을 위한 Access Token을 발급하기 위해서는 자원 소유자는 보호 자원과 클라이언트를 정의하고 접근 권한 또한 정의해야 한다.  바로 Keycloak 설정을 통해 가능하다. 이런 보안 설정 대상이 되며 꼭 이해해야 하는 Keyclock의 몇 가지 주요 객체(개념)들이 있다.

1. realm
	<p>
	사용자, 자격 증명, 역할 및 그룹 집합 등을 관리하는 객체다. 사용자는 realm에 속하고 자격증명을 통해 인증을 받을 것이다. realm들은 서로 격리되어 있고 realm에 속한 사용자만 관리되고 인증된다.
	</p>
	
2. client
	<p>
	OAuth2 스펙에 정의된 client와 다르지 않은 개념이다. 보호 자원을 제공하는 자원 서버에게 인가를 받아야 하는 대상이다. Keycloak은 자원 서버가 인가를 할 수 있도록 자원 소유자가 정의한 클라이언트에 정의된 권한을 인증된 Realm 사용자에게 부여한다. 사용자는 여러 클라이언트의 권한을 부여 받을 수 있지만 클라이언트에 부여된 이름과 비밀키(Secret Key)가 사용자 인증에 필요한 자격 증명의 일부가 되기 때문에 특정 클라이언트의 권한만 부여 받아 클라이언트가 접근하려는 자원에만 자원서버에 의해 인가된다.
	</p>
	
4. role
	<p>
	자원 소유자는 사용자와 클라이언트에 대한 권한과 액세스 수준을 정의하고 관리하는 데 role를 사용할 수 있다. 롤은 사용자나 클라이언트가 자원에 대해 수행할 수 있는 작업을 지정하는 데 사용된다.
	</p>
	
6. user
	<p>
	사용자는 자격 증명을 통해 인증을 받고 자원 소유자가 정의해 놓은 role로 표현된 권한을 부여받고 자원 서버에 인가되어 자원에 접근할 수 있게 된다. Keycloak에서는 특정 realm에 user를 추가하는 설정을 통해 이를 가능하게 한다. 
	</p>
	
#### 6. Keycloak Security Configuration : realm
1. Ream 생성
	![ef87fd813373a21f110a4d92e35bd883.png](../../../_resources/ef87fd813373a21f110a4d92e35bd883.png)
	- Add Realm 버튼 클릭
	<br>
	
	![5d132969b0416fe04fcf2b2ce508113f.png](../../../_resources/5d132969b0416fe04fcf2b2ce508113f.png)
	- Name 입력
	- Create 버튼 클릭
	<br>

2. 생성 완료
	![83b7e0b4645c2152f2d939b02fb0a5bb.png](../../../_resources/83b7e0b4645c2152f2d939b02fb0a5bb.png)
	- Token 탭에서 Token의 세부사항을 설정 할 수 있다.
	<br>

#### 7. Keycloak Security Configuration : clients
1. Client 생성
	![d7a7d610a8c7dc91a283504cb999b9d6.png](../../../_resources/d7a7d610a8c7dc91a283504cb999b9d6.png)
	- Create 버튼 클릭
	<br>
	
	![37d4dd0cea6d94375a99de6f0b8fc55d.png](../../../_resources/37d4dd0cea6d94375a99de6f0b8fc55d.png)
	- client id를 지정한다.
	- 프로토콜은 openid-connect 이다.
	- Root URL은 필요 없다.
	<br>

	![d5b742ff3f3692dd1e8a2b95dd1d5df8.png](../../../_resources/d5b742ff3f3692dd1e8a2b95dd1d5df8.png)
	<br>
	
2. Clinet 설정
	![5265491b77e6082117669d6a7e2af3d1.png](../../../_resources/5265491b77e6082117669d6a7e2af3d1.png)
	- Enabled : ON
	- Client Protocol : openid-connect
	- Access Type : Confidential
	- Standard Flow Enabled : OFF (Authorization Code Flow X)
	- Direct Access Grant Enabled : On (Resource Owner Password Credentials O)
	- Service Account Enabled : On
	- Authorization Grant Enabled : On

#### 8. Keycloak Security Configuration : roles
Keyloak에서는 role로 표현되는 권한을 realm user 에게 부여할 수 있다. 따라서 권한을 먼저 부여하는 설정을 위해 role를 생성해야 한다. role은 크게 두 realm role과 client role 이렇게 두 가지로 나눌 수 있다. 권한을 표현하고 realm user에게 부여한다 라는 측면에서는 별다른 차이가 없지만 user 가 특정 클라이언트만 사용하는 경우라면 client role만 부여하면 될 것이다. 하지만 여러 클라이언트의 운용해야 하고 공통으로 관리해야 하는 권한이 있다면 realm role이 더 적합하다.  
1. Role 생성
	![7ce00706ca6e99fbd1f16ed8f63f8150.png](../../../_resources/7ce00706ca6e99fbd1f16ed8f63f8150.png)
	- Add Role 버튼 클릭
	<br>

	![d805c7a8e24e7bf2a40ac4c5520d6916.png](../../../_resources/d805c7a8e24e7bf2a40ac4c5520d6916.png)
	- Role Name 등록 (보통 대문자로...)
	- Save 버튼 클릭
	<br>

	![334a48a3bf7d71875a83b1553c2466f7.png](../../../_resources/334a48a3bf7d71875a83b1553c2466f7.png)
	-	생성 완료
	-	Composite Role은 합성 role 설정에 사용한다. 여러 복수 role를 합성해서 생성 할 수 있는 합성 role 설정도 할 수 있다.
	<br>
	
	![c5c8742336cc355fca4d4f862a7fefb3.png](../../../_resources/c5c8742336cc355fca4d4f862a7fefb3.png)
	-	같은 방식으로 READ role도 생성 한다.
	<br>
	
#### 9. Keycloak Security Configuration : users
이제 실제적으로 인증과 권한 부여 접근 주체라고 볼 수 있는 user를 생성해 보자. 
1.	User 생성
	![441fbf70c0ce2ae071ba9ac3744015e7.png](../../../_resources/441fbf70c0ce2ae071ba9ac3744015e7.png)
	- Add 버튼 클릭
	<br>
	
	![0e71b76cacb06918747a1796c0d29e9d.png](../../../_resources/0e71b76cacb06918747a1796c0d29e9d.png)
	- Username: 신원 확인에 반드시 필요하다.
	- Email: 입력시, 신원 확인에 email 주소도 사용할 수 있다.
	- User Enabled: ON
	- Save 버튼 클릭
	<br>
	
	![9fb00ae9d6a01732dc26b9dbc669ef96.png](../../../_resources/9fb00ae9d6a01732dc26b9dbc669ef96.png)
	- 생성 완료
	- 생성 후, 해야하는 중요한 설정은 Credentials, Role Mapping 이다.
	<br>

2. Credentials (password) 설정
	![10698b495d2b6fb4655d66abec4b5e2d.png](../../../_resources/10698b495d2b6fb4655d66abec4b5e2d.png)
	- Password : 입력
	- Temporary: OFF
	- Set Password 버튼 클릭
	<br>
	
	![cbd8c024f35d20bed08ba97a707225e8.png](../../../_resources/cbd8c024f35d20bed08ba97a707225e8.png)
	- password 설정 완료

2. Role Mapping
	![39d7c474149d1d7d5f453af25d8dcaf1.png](../../../_resources/39d7c474149d1d7d5f453af25d8dcaf1.png)
	- Client Roles 선택
	- emaillist 클라이언트 선택하면 등록한 emaillist에 생성한 READ, WRITE role이 보인다.
	<br>
	
	![c3f859f914900a108cae5b8884a18520.png](../../../_resources/c3f859f914900a108cae5b8884a18520.png)
	- READ role을 Assigne Role에 추가하여 읽기 권한을 부여 했다.

3. Username 'michol' 사용자 생성(READ, WRITE Roles)
	![7a7ca6eaaf8d8b3a73c6dde1c0823887.png](../../../_resources/7a7ca6eaaf8d8b3a73c6dde1c0823887.png)
	<br>
	
	![6690d076fe00a0247832eda1dbd05c03.png](../../../_resources/6690d076fe00a0247832eda1dbd05c03.png)
	<br>
	
	![97934f9ca48378a93ac8548c4ab2b970.png](../../../_resources/97934f9ca48378a93ac8548c4ab2b970.png)
	<br>
	
#### 10. OpenID Endpoint Configuration
Test를 하기전에 Keycloak의 Realm Settings의 다음 링크에서 poscodx2023-realm의 가용한 엔드포인트들을 먼저 확인해 보자.

![0a7ac588eb544d481782d1d857cacc93.png](../../../_resources/0a7ac588eb544d481782d1d857cacc93.png)

OpenID Endpoint Configuration 링크 클릭!

![87033fff91231c98c9b17f8bfa46e15a.png](../../../_resources/87033fff91231c98c9b17f8bfa46e15a.png)


#### 11. Issue Access Token
1. Client 인증
	Authorization Grant Flow 방식의 사용자 권한 부여를 받는 flow에서는 사용자 인증과 함께 Client 인증이 추가적으로 필요하다. Client 인증에 필요한 정보는  HTTP 기본 인증(Basic Authentication) 방식으로 전달된다. 따라서 다음의 헤더 내용이 더 추가된다.   
	
	```
	Authorization Basic ZW1haWxsaXN0OlhRa2x5TVNRNWwyd30sd1xJqadf...
	```
	
	-	Header 이름: "Authorization"
	-	Header 내용: "Basic" + " " + Base64(ClientName + ":" + Clinet Secret)
	-	Client Secret 확인
		![9c0194852a9bf06ea35d0f5a98d537cf.png](../../../_resources/9c0194852a9bf06ea35d0f5a98d537cf.png)
	- 	Basic Auhtentication Header 설정(Talend Chrome Plugin)
		![24c95621d6a0f526b1963d9ce3591113.png](../../../_resources/24c95621d6a0f526b1963d9ce3591113.png)
![0137ff91e78d035910a50c633ea77b43.png](../../../_resources/0137ff91e78d035910a50c633ea77b43.png)
		
2. 사용자 인증
	사용자 인증에 필요한 Credential은 POST 방식으로 HTTP 바디(HTTP Form Data)에 포함되어 전달된다.
	-  grant_type : password
	-  username : username or email
	-  password

3. Keycloak EndPoint
	http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	
4. 테스트
	![7b239e79c6073dbb3af7dd5c27dbbd15.png](../../../_resources/7b239e79c6073dbb3af7dd5c27dbbd15.png)

5.	jwt.io 에서 Access Token 확인하기
	![70df91da63bbf37c14290d4f739ffdec.png](../../../_resources/70df91da63bbf37c14290d4f739ffdec.png)

#### 12. Refresh Access Token via Refresh Token
Resource Server의 인가를 목적으로 보내는 Access Token 이 유효 기간이 만료되거나 클라이언트 애플리케이션의 Token 저장 방식에 따라 휘발된 경우 Fresh Token으로 Access Token을 재발급 받아 리소스 접근 인가를 위해 사용할 수 있다.
1. Client 인증
	앞의 Access Token을 발급 받았을 때와 마찬가지로 클라이언트 인증을 위한 Authorization Header가 세팅되어야 한다.
	
2. 사용자 인증
	Access Token을 재발급 받기 위해 사용자의 Credential은 필요하지 않다. Refresh Token을 사용하기 때문이다. 따라서 grant_type이 password에서 fresh_token으로만 변경하면 된다. post 방식으로 보내야 하는 form data는 다음과 같다
	- grant_type: fresh_token
	- refresh_token: eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI...

3. Keycloak EndPoint
	http://localhost:5555/realms/poscodx2023-realm/protocol/openid-connect/token
	
4. 테스트
	![29584e432897108e3841dc5cd3792952.png](../../../_resources/29584e432897108e3841dc5cd3792952.png)




