package com.poscodx.msa.service.emaillist.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
// @ActiveProfiles("test")
public class SecurityConfigTest {

	    private FilterChainProxy filterChainProxy;

	    @MockBean
	    private JwtDecoder jwtDecoder;
	    
	    @BeforeEach
	    public void setup(WebApplicationContext context) {
	        filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);
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

	        assertEquals(10, filters.size());

	        // All Filters
	        for(Filter filter : filters) {
	            System.out.println(filter.getClass());
	        }

	        // 7th BearerTokenAuthenticationFilter
	        assertEquals("BearerTokenAuthenticationFilter", filters.get(4).getClass().getSimpleName());

	        // 10th AuthorizationFilter
	        assertEquals("AuthorizationFilter", filters.get(9).getClass().getSimpleName());
	    }	    
}
