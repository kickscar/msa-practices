## 3. Filters Order & Configuration
#### Practical Configuration
1. @EnableWebSecurity 사용
2. FilterChainProxy Bean 암시적 생성
3. WebSecurityCustomizer(Deprecated)
	- Spring Security Filter Chain(SecurityFilterChain)의 제외 대상 URI 설정
	- SecurityFilterChain의 Access Control로 제외 대상 URI 설정이 가능하기 때문에
	- 
4. HttpSecurity(SecurityBuilder)를 사용한 SecurityFilterChain 암시적 생성

####  Configuration1 - XML Default
1. Test
	- configuration: me/kickscar/spring/security/config/configurer/SecurityConfig01.xml
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig01Test
2. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter    (default)  7
	10. DefaultLoginPageGeneratingFilter        (default)  8
	11. DefaultLogoutPageGeneratingFilter       (default)  9
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter               (default)  10
	14. RequestCacheAwareFilter                 (default)  11
	15. SecurityContextHolderAwareRequestFilter (default)  12
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  13
	19. SessionManagementFilter                 (default)  14
	20. ExceptionTranslationFilter              (default)  15
	21. FilterSecurityInterceptor               (default)  16
	</pre>
	
#### Configuration2 - SecurityBuilder Default 
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig02
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig02Test
2. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter    
	10. DefaultLoginPageGeneratingFilter        
	11. DefaultLogoutPageGeneratingFilter       
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter               
	14. RequestCacheAwareFilter                 (default)  7
	15. SecurityContextHolderAwareRequestFilter (default)  8
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  9
	19. SessionManagementFilter                 (default)  10
	20. ExceptionTranslationFilter              (default)  11
	21. FilterSecurityInterceptor
	</pre>
 
	
#### Configuration3 - Custom SecurityBuilder
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig03
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig03Test
2. formLogin()
	- FormLoginConfigurer
3. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter               7 *
	10. DefaultLoginPageGeneratingFilter                   8 *
	11. DefaultLogoutPageGeneratingFilter                  9 *
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter               
	14. RequestCacheAwareFilter                 (default)  10
	15. SecurityContextHolderAwareRequestFilter (default)  11
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  12
	19. SessionManagementFilter                 (default)  13
	20. ExceptionTranslationFilter              (default)  14
	21. FilterSecurityInterceptor
	</pre>
	
#### Configuration4 - Custom SecurityBuilder
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig04
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig04Test
2. formLogin()
	- FormLoginConfigurer
3. httpBasic()
	- HttpBasicConfigurer
	-  configure HTTP Basic authentication  
4. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter               7
	10. DefaultLoginPageGeneratingFilter                   8
	11. DefaultLogoutPageGeneratingFilter                  9
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter                          10 *
	14. RequestCacheAwareFilter                 (default)  11
	15. SecurityContextHolderAwareRequestFilter (default)  12
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  13
	19. SessionManagementFilter                 (default)  14
	20. ExceptionTranslationFilter              (default)  15
	21. FilterSecurityInterceptor
	</pre>
	
#### Configuration 5 - CustomSecurityBuilder 
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig05
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig05Test
2. formLogin()
	- FormLoginConfigurer
3. httpBasic()
	- HttpBasicConfigurer
	- configure HTTP Basic authentication  
4. authorizeRequests() [Deprecated]
	- Access Control
	- Authorization Configurer
5. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter               7
	10. DefaultLoginPageGeneratingFilter                   8
	11. DefaultLogoutPageGeneratingFilter                  9
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter                          10
	14. RequestCacheAwareFilter                 (default)  11
	15. SecurityContextHolderAwareRequestFilter (default)  12
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  13
	19. SessionManagementFilter                 (default)  14
	20. ExceptionTranslationFilter              (default)  15
	21. FilterSecurityInterceptor                          16  *  [deprecated]
	</pre>

#### Configuration 6 - Custom SecurityBuilder
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig06
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig06Test
2. formLogin()
	- FormLoginConfigurer
3. httpBasic()
	- HttpBasicConfigurer
	- configure HTTP Basic authentication
4. authorizeHttpRequests()
	- Access Control
	- Authorization Configurer
5. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter               7
	10. DefaultLoginPageGeneratingFilter                   8
	11. DefaultLogoutPageGeneratingFilter                  9
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter                          10
	14. RequestCacheAwareFilter                 (default)  11
	15. SecurityContextHolderAwareRequestFilter (default)  12
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  13
	19. SessionManagementFilter                 (default)  14
	20. ExceptionTranslationFilter              (default)  15
	21. AuthorizationFilter                                16  *
	</pre>

#### Configuration 7 - Custom SecurityBuilder
1. Test
	- configuration: me.kickscar.spring.security.config.configurer.SecurityConfig07
	- test: me.kickscar.spring.security.config.configurer.SecurityConfig07Test
2. formLogin()
	- FormLoginConfigurer
	- loginpage(...): the login page to redirect to if authentication is required
3. authorizeHttpRequests()
	- Access Control
	- Authorization Configurer
4. filters order
	<pre>
	01. ChannelProcessingFilter
	02. DisableEncodeUrlFilter                  (default)  1
	03. SecurityContextPersistenceFilter        (default)  2
	04. WebAsyncManagerIntegrationFilter        (default)  3
	05. ConcurrentSessionFilter
	06. HeaderWriterFilter                      (default)  4
	07. CsrfFilter                              (default)  5
	08. HeaderWriter                            (default)  6
	09. UsernamePasswordAuthenticationFilter               7
	10. DefaultLoginPageGeneratingFilter                   x               
	11. DefaultLogoutPageGeneratingFilter                  x                  
	12. CasAuthenticationFilter
	13. BasicAuthenticationFilter                          x                          
	14. RequestCacheAwareFilter                 (default)  8
	15. SecurityContextHolderAwareRequestFilter (default)  9
	16. JaasApiIntegrationFilter
	17. RememberMeAuthenticationFilter
	18. AnonymousAuthenticationFilter           (default)  10
	19. SessionManagementFilter                 (default)  11
	20. ExceptionTranslationFilter              (default)  12
	21. AuthorizationFilter                                13
	</pre>

#### Practical Configuration (Recommanded) - Custom SecurityBuilder
[참고] /monolithics/mysite06 