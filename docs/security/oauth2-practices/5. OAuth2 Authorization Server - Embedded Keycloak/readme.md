## 5. OAuth2 Authorization Server - Embedded Keycloak 

#### OAuth2 (Open Authorization 2.0) ?
Token 기반의 보안 프레임워크으로 인증(Authentication)과 인가(Authorization)에 대한 패턴을 정의

#### OAuth2 Security Terminolgy
1. 보호 자원(Resource)
	<p>
	자격(권한, Authority)이 있는 접근에 대해서만 제공되는 보호대상이 되는 자원을 의미한다. MSA에서는 서비스들의 API 이다. 
	</p>
	
2. 자원 서버(Resource Server) 
	<p>
	보호 대상이 되는 자원을 제공하는 서버다. MSA에서는 구현 서비스가 된다.
	</p>

3. 클라이언트(Client) 
	<p> 
	보호 자원에 접근하려는 애플리케이션이다. 보통, 서비스들의 API를 호출한다. 당연히, 클라이언트는 보호 자원을 제공하는 자원 서버에게 허락(인가)를 받아야 한다. 자원 서버가 자원 제공과 함께 이 인가를 해주는 것이 어찌보면 당연하다. 그리고 이런 방식으로 서비스를 많이 개발한다. 하지만, MSA에서는 얘기가 조금 다르다. MSA를 구성하는 다수의 서비스(자원 서버)들이 이 인가 작업을 개별적으로 하게되면 클라이언트가 다수의 서비스에 자원 요청을 할 때 마다 서비스별로 인증과 인가를 따로따로 받아야 한다. 불가능한 것은 아니지만 불편하다. 이 불편함을 해결할 수 있는 것이 바로  OAuth2 이다.  OAuth2는 딱! 한 번의 인증과 인가로 여러 자원 서버의 자원을 요청할 수 있는 방법을 제공한다.
 	</p>
 
4. 자원 소유자(Resource Owner)
	<p>
	자원에 접근하려는 클라이언트에게 접근 자격(Grant)을 부여하는 주체라는 다소 추상적인 개념이다. 실제, OAurth2 구현에서는 이 실제적 수행은 인증/인가 서버(Authorization Server)가 한다.  자원 소유자가 하는 개념적 역할 다음과 같은 것이 있다.
	</p>	
	- 클라이언트(Client, Application)가 접근 가능한 자원(서비스) 정의
	- 클라이언트의 사용자에 대한 접근 권한(Authority) 정의
	- 클라이언트의 자원 접근에 대한 제어	
	<p>	 
	 자원 소유자가 등록한 클라이언트는 이름과 비밀키(Secret Key)가 지정된다.  이름과 키 조합은 인증/인가 서버가 발급(issue)하는 Access Token의 자격 증명(Credential)의 일부가 된다. 앞에서도 언급했지만 자원 소유자가 실제 OAuth2 구현에서 실체가 있는 것이 아니다. 주 요 역할이 자원 접근에 대한 정의와 제어라 하였는데 이는 OAuth2 구현에서 자원 서버의 보안 코드 또는 인증/인가 서버의 설정 등으로 보여 질 수는 있는 OAuth2 권한 부여 플로우 상의 추상적인 개념일 뿐이다. 
	</p>	 

5. 인가(Authorization) 
	<p>
	특정 자원에 대한 접근 권한(Authority)를 부여(Grant) 하는 것이다. 접근은 클라이언트 애플리케이션의 API 호출의 URL 그리고 HTTP Method의 조합으로 이루어지기 때문에 실제로는 애플리케이션의 사용자(Principal, 접근 주체) 들에게 부여된다 볼 수 있다. 따라서 사용자의 신원(Identity)을 확인하는 인증(Authentication)이 인가에서는 필수이고 먼저 선행된다. 따라서 개념적으로는 인가에는 인증이 어느정도 포함되어 있다.
	</p>	

6. 인증(Authentication)
	<p> 
	특정 자원 접근에 인가(Authorization)되기 위해서는 접근 권한(Authority)을 부여(Grant) 받아야 하고 접근 주체(Principal)인 사용자는 자격 증명(Credentials)을 제출해 신원(Identity)을 먼저 확인 받아야 한다. 이를 인증이라 한다. 복잡하게 보안스럽게 얘기했지만 쉽게 말하면 로그인 또는 사인인(Sign-in)이다. 보안 측면에서 보면 sign-in이 더 맞는 말이다.
	</p>
	
7. 인증/인가 서버 (Authorization Server)
	<p>
	인증/인가를 수행하는 서버로 클라이언트의 접근 자격을 확인하고 Access Token을 발급하여 권한을 부여하는 역할을 수행한다. 개념적으로 자원 소유자가 정의한 자원 접근에 대한 권한을 자원에 접근하는 클라이언트에게 실제 부여하는 수행 작업을 한다. MSA에서 클라이언트는 인증/인가 서버로의 한 번의 인증과 인가로 부여 받은 권한에 맞는 모든 서비스 접근이 가능해진다. 
	</p>

#### Authorization Grant Flow
1. Authorization Code Grant

2. Implicit Grant 

3. Resource Owner Password Credentials Grant

4. Client Credentials Grant 








