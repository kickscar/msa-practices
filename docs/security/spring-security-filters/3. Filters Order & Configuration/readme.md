## 3. Filters Order & Configuration

#### 1. XML Default Configuration
1. me.kickscar.spring.security.config.configurer.SecurityConfig01.xml
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
	
#### 2. SecurityBuilder Default Configuration
1. me.kickscar.spring.security.config.configurer.SecurityConfig02
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
	
#### 3. SecurityBuilder Custom Configuration 01
1. me.kickscar.spring.security.config.configurer.SecurityConfig03
2. + formLogin()
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
	
#### 4. SecurityBuilder Custom Configuration 02
1. me.kickscar.spring.security.config.configurer.SecurityConfig04
2. + formLogin()
	+ httpBasic() 
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
	
#### 5. SecurityBuilder Custom Configuration 03
1. me.kickscar.spring.security.config.configurer.SecurityConfig05
2. + formLogin()
	+ httpBasic()
	+ authorizeRequests() [deprecated]
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

#### 6. SecurityBuilder Custom Configuration 04
1. me.kickscar.spring.security.config.configurer.SecurityConfig06
2. + formLogin()
	+ httpBasic()
	+ authorizeHttpRequests()
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

#### 7. SecurityBuilder Custom Configuration 05
1. me.kickscar.spring.security.config.configurer.SecurityConfig07
2. + formLogin()
	+ loginpage(...)
	+ authorizeHttpRequests()
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

#### 8. SecurityBuilder Custom Configuration (Practical Recommendation)
[참고] /monolithics/mysite06 