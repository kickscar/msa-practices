package me.kickscar.spring.security.config.configurer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations={"classpath:me/kickscar/web/config/WebConfig.xml", "classpath:me/kickscar/spring/security/config/explicit/SecurityConfig01.xml"})
@WebAppConfiguration
public class SecurityConfig01Test {
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
        assertEquals(2, SecurityFilterChains.size());
    }

    @Test
    public void testSecurityFilters() {
        SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(1);
        List<Filter> filters =  securityFilterChain.getFilters();

        assertEquals(16, filters.size());

        // All Filters
        for(Filter filter : filters) {
            System.out.println(filter.getClass());
        }
    }

    @Test
    public void testWebSecurity() throws Throwable {
        mvc
                .perform(get("/assets/images/logo.png"))
                .andExpect(status().isOk());
    }

    @Test
    public void testHttpSecurity() throws Throwable {
        mvc
                .perform(get("/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}