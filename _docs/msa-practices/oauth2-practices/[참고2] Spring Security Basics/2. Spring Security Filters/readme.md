## 2. Spring Security Filters

#### 01. ChannelProcessingFilter

#### 02. DisableEncodeUrlFilter
세션 ID가 URL에 포함되는 것을 막기 위해 HttpServletResponse를 사용해서 URL이 인코딩 되는 것을 막기 위한 필터

#### 03. SecurityContextPersistenceFilter
SecurityContext가 없으면 만들어주는 필터, SecurityContext는 Authentication 객체를 보관하는 인터페이스 SecurityContext를 통해 한 요청에 대해서 어떤 필터에서도 같은 Authentication 객체를 사용

#### 04. WebAsyncManagerIntegrationFilter
SpringSecurityContextHolder는 기본적으로 ThreadLocal 기반으로 동작하는데, 비동기와 관련된 기능을 쓸 때 SecurityContext를 사용할 수 있도록 만들어주는 필터

#### 05. ConcurrentSessionFilter

#### 06. HeaderWriterFilter
응답에 Security와 관련된 헤더 값을 설정해주는 필터

#### 07. CsrfFilter
CSRF 공격을 방어하기 위한 설정을 하는 필터

#### 08. HeaderWriter
로그아웃 요청을 처리하는 필터

#### 09. UsernamePasswordAuthenticationFilter
username과 password를 쓰는 form 기반 인증을 처리하는 필터, AuthenticationManager를 통한 인증을 실행, 성공하면 Authentication 객체를 SecurityHolder에 저장한 후AuthenticationSuccessHandler를 실행 실패하면 AuthenticationFailureHandler를 실행

#### 10. DefaultLoginPageGeneratingFilter
로그인 기본 페이지를 생성하는 필터

#### 11. DefaultLogoutPageGeneratingFilter
로그아웃 기본 페이지를 생성하는 필터

#### 12. CasAuthenticationFilter

#### 13. BasicAuthenticationFilter
HTTP header에 인증 값을 담아 보내는 BASIC 인증을 처리하는 필터

#### 14. RequestCacheAwareFilter
인증 처리 후 원래의 Request 정보로 재구성하는 필터

#### 15. SecurityContextHolderAwareRequestFilter
서블릿 API 보안 메서드를 구현하는 요청 래퍼로 서블릿 요청을 채우는 필터

#### 16. JaasApiIntegrationFilter

#### 17. RememberMeAuthenticationFilter

#### 18. AnonymousAuthenticationFilter
이 필터에 올 때까지 사용자가 인증되지 않았다면, 이 요청은 익명의 사용자가 보낸 것으로 판단할 수 있다. 이 익명 사용자에 관한 처리를 하는 필터

#### 19. SessionManagementFilter
세션 생성 전략을 설정하는 필터, 최대 동시 접속 세션을 설정, 유효하지 않은 세션으로 접근했을 때의 처리, 세션 변조 공격 방지 등의 처리

#### 20. ExceptionTranslationFilter
필터 처리 과정에서 인증 예외 또는 인가 예외가 발생한 경우 해당 예외를 잡아서 처리하는 필터

#### 21. FilterSecurityInterceptor
인가를 결정하는 AccessDicisionManager에게 접근 권한이 있는지 확인하고 처리하는 필터 앞 필터들을 통과할 때 인증 또는 인가에 문제가 있으면 ExceptionTranslationFilter로 예외를 던진다.