package com.poscodx.emaillist.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@WebAppConfiguration
@ActiveProfiles("test")
public class ConfigTest {
 	@MockBean
	private ClientRegistrationRepository clientRegistrationRepository;

    private FilterChainProxy filterChainProxy;
    
    @BeforeEach
    public void setup(WebApplicationContext context) {
        filterChainProxy = (FilterChainProxy)context.getBean("springSecurityFilterChain", Filter.class);
        clientRegistrationRepository = context.getBean(ClientRegistrationRepository.class);
    }
    
    @Test
    public void testSecurityFilterChains() {
        List<SecurityFilterChain> SecurityFilterChains = filterChainProxy.getFilterChains();
        assertEquals(1, SecurityFilterChains.size());
    }
    
    @Test
    public void testSecurityFilters() {
        SecurityFilterChain securityFilterChain = filterChainProxy.getFilterChains().get(0);
        List<Filter> filters =  securityFilterChain.getFilters();

        assertEquals(13, filters.size());

        // All Filters
        for(Filter filter : filters) {
            System.out.println(filter.getClass());
        }
    }
}
