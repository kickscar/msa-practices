package me.kickscar.spring.security.config.configurer;

import me.kickscar.web.config.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={WebConfig.class, SecurityConfig07.class})
@WebAppConfiguration
public class SecurityConfig07Test {
    private MockMvc mvc;

    private FilterChainProxy filterChainProxy;

    @BeforeEach
    public void setup(WebApplicationContext context) {
        filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter(new DelegatingFilterProxy(filterChainProxy), "/*")
                .build();
    }

    @Test
    public void testSecurityFilterChains() {
        List<SecurityFilterChain> SecurityFilterChains = filterChainProxy.getFilterChains();
        assertEquals(1, SecurityFilterChains.size());
    }

    @Test
    public void testSecurityConfiguration(WebApplicationContext context) {
        SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
        List<Filter> filters =  securityFilterChain.getFilters();

        assertEquals(14, filters.size());

        // All Filters
        for(Filter filter : filters) {
            System.out.println(filter.getClass());
        }

        /*
            -- Filter Ordering --


             01. ChannelProcessingFilter
             02. DisableEncodeUrlFilter                  (default)  1   : 세션 ID가 URL에 포함되는 것을 막기 위해 HttpServletResponse를 사용해서 URL이 인코딩 되는 것을 막기 위한 필터
             03. WebAsyncManagerIntegrationFilter        (default)  2   : SpringSecurityContextHolder는 기본적으로 ThreadLocal 기반으로 동작하는데, 비동기와 관련된 기능을 쓸 때에도 SecurityContext를 사용할 수 있도록 만들어주는 필터
             04. SecurityContextPersistenceFilter        (default)  3   : SecurityContext가 없으면 만들어주는 필터, SecurityContext는 Authentication 객체를 보관하는 인터페이스, SecurityContext를 통해 한 요청에 대해서 어떤 필터에서도 같은 Authentication 객체를 사용
             05. ConcurrentSessionFilter                                :
             06. HeaderWriterFilter                      (default)  4   : 응답에 Security와 관련된 헤더 값을 설정해주는 필터
             07. CsrfFilter                              (default)  5   : CSRF 공격을 방어하기 위한 설정을 하는 필터
             08. LogoutFilter                            (default)  6   : 로그아웃 요청을 처리하는 필터
             09. UsernamePasswordAuthenticationFilter               7   : username과 password를 쓰는 form 기반 인증을 처리하는 필터, AuthenticationManager를 통한 인증을 실행, 성공하면 Authentication 객체를 SecurityHolder에 저장한 후 AuthenticationSuccessHandler를 실행, 실패하면 AuthenticationFailureHandler를 실행
             10. DefaultLoginPageGeneratingFilter                       : 로그인 기본 페이지를 생성하는 필터
             11. DefaultLogoutPageGeneratingFilter                      : 로그아웃 기본 페이지를 생성하는 필터
             12. CasAuthenticationFilter                                :
             13. BasicAuthenticationFilter                          8   : HTTP header에 인증 값을 담아 보내는 BASIC 인증을 처리하는 필터
             14. RequestCacheAwareFilter                 (default)  9   : 인증 처리 후 원래의 Request 정보로 재구성하는 필터
             15. SecurityContextHolderAwareRequestFilter (default)  10  : 서블릿 API 보안 메서드를 구현하는 요청 래퍼로 서블릿 요청을 채우는 필터
             16. JaasApiIntegrationFilter                               :
             17. RememberMeAuthenticationFilter                         :
             18. AnonymousAuthenticationFilter           (default)  11  : 이 필터에 올 때까지 사용자가 인증되지 않았다면, 이 요청은 익명의 사용자가 보낸 것으로 판단할 수 있다. 이 익명 사용자에 관한 처리를 하는 필터
             19. SessionManagementFilter                 (default)  12  : 세션 생성 전략을 설정하는 필터, 최대 동시 접속 세션을 설정, 유효하지 않은 세션으로 접근했을 때의 처리, 세션 변조 공격 방지 등의 처리
             20. ExceptionTranslationFilter              (default)  13  : 필터 처리 과정에서 인증 예외 또는 인가 예외가 발생한 경우 해당 예외를 잡아서 처리하는 필터
             21. AuthorizationFilter                                14  : 접근 권한이 있는지 확인하고 처리하는 필터
        */

        // DefaultLoginPageGeneratingFilter, DefaultLogoutPageGeneratingFilter removed
        assertEquals(0, filters.stream().filter(f -> f instanceof DefaultLoginPageGeneratingFilter || f instanceof DefaultLogoutPageGeneratingFilter).count());
    }

    @Test
    public void testWebSerity() throws Throwable {
        mvc
                .perform(get("/assets/images/logo.png"))
                .andExpect(status().isOk());
    }

    @Test
    public void testHttpSerity() throws Throwable {
        mvc
                .perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}