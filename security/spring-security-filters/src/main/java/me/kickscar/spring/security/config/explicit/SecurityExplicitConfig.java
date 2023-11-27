package me.kickscar.spring.security.config.explicit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.security.web.context.*;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

@Configuration
public class SecurityExplicitConfig {
    @Bean
    public FilterChainProxy springSecurityFilterChain() {
        List<SecurityFilterChain> SecurityFilterChains = Arrays.asList(
                new DefaultSecurityFilterChain(new AntPathRequestMatcher("/favicon.ico")),
                new DefaultSecurityFilterChain(new AntPathRequestMatcher("/assets/**")),
                new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
                        securityContextPersistenceFilter(),
                        logoutFilter(),
                        usernamePasswordProcessingFilter(),
                        anonymousAuthenticationFilter(),
                        exceptionTranslationFilter(),
                        filterSecurityInterceptor()
                )
        );

        return new FilterChainProxy(SecurityFilterChains);
    }


    /*** SecurityContextPersistenceFilter ******************/
    @Bean
    public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
        return new SecurityContextPersistenceFilter();
    }

    @Bean
    public SecurityContextHolderFilter securityContextHolderFilter() {
        return new SecurityContextHolderFilter(new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        ));
    }

    /*** LogoutFilter **************************************/
    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(logoutSuccessHandler(), logoutHandler());
        logoutFilter.setFilterProcessesUrl("/user/logout");

        return logoutFilter;
    }

    // LogoutSuccessHandler
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl("/");
        logoutSuccessHandler.setAlwaysUseDefaultTargetUrl(true);

        return logoutSuccessHandler;
    }

    // LogoutHandler
    @Bean
    public LogoutHandler logoutHandler() {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.setInvalidateHttpSession(true);

        return logoutHandler;
    }



    /*** UsernamePasswordAuthenticationFilter **************/
    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordProcessingFilter() {
        UsernamePasswordAuthenticationFilter usernamePasswordProcessingFilter = new UsernamePasswordAuthenticationFilter();

        usernamePasswordProcessingFilter.setAuthenticationManager(authenticationManager());
        usernamePasswordProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        usernamePasswordProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        usernamePasswordProcessingFilter.setFilterProcessesUrl("/user/auth");
        usernamePasswordProcessingFilter.setUsernameParameter("email");
        usernamePasswordProcessingFilter.setPasswordParameter("password");

        return usernamePasswordProcessingFilter;
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());

        return authenticationProvider;
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService(){

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        };
    }

    // AuthenticationSuccessHandler
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        authenticationSuccessHandler.setDefaultTargetUrl("/");

        return authenticationSuccessHandler;
    }

    // AuthenticationFailureHandler
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
        authenticationFailureHandler.setDefaultFailureUrl("/user/login");

        return authenticationFailureHandler;
    }


    /*** AnonymousAuthenticationFilter **********************/
    @Bean
    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
        return new AnonymousAuthenticationFilter("BF93JFJ091N00Q7HF");
    }


    /*** ExceptionTranslationFilter *************************/
    @Bean
    public ExceptionTranslationFilter exceptionTranslationFilter() {
        ExceptionTranslationFilter exceptionTranslationFilter = new ExceptionTranslationFilter(authenticationEntryPoint());
        exceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandler());

        return exceptionTranslationFilter;
    }

    // AuthenticationEntryPoint
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/user/login");
    }

    // AccessDeniedHandler
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/WEB-INF/views/error/403.jsp");

        return accessDeniedHandler;
    }




    /*** FilterSecurityInterceptor *************************/
    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
        filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());

        return filterSecurityInterceptor;
    }

    // AuthenticationManager

    // FilterSecurityInterceptor
    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
        return new DefaultFilterInvocationSecurityMetadataSource(new LinkedHashMap<>(){{
            put(new RegexRequestMatcher("^/admin/?.*$", null), Arrays.asList(() -> "ROLE_ADMIN"));
            put(new RegexRequestMatcher("^/board/?(write|reply|delete)$", null), Arrays.asList(() -> "ROLE_USER"));
            put(new RegexRequestMatcher("^/user/update$", null), Arrays.asList(() -> "ROLE_USER"));
        }});
    }

    // AccessDecisionManager
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new AffirmativeBased(Arrays.asList(
                authenticatedVoter(),
                roleVoter(),
                webExpressionVoter()

        ));
    }

    @Bean
    public AuthenticatedVoter authenticatedVoter() {
        return new AuthenticatedVoter();
    }
    @Bean
    public RoleVoter roleVoter() {
        return new RoleVoter();
    }
    @Bean
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(securityExpressionHandler());

        return webExpressionVoter;
    }
    @Bean
    public SecurityExpressionHandler securityExpressionHandler() {
        return new DefaultWebSecurityExpressionHandler();
    }
}
