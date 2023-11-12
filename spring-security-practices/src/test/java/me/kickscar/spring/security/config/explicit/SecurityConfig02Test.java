package me.kickscar.spring.security.config.explicit;

import me.kickscar.web.config.WebConfig;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes={WebConfig.class, SecurityConfig02.class})
@WebAppConfiguration
public class SecurityConfig02Test {
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

        assertEquals(4, filters.size());

        for(Filter filter : filters) {
            System.out.println(filter.getClass());
        }
    }

    @Test
    public void testWebSerity() throws Throwable {
        mvc
                .perform(get("/assets/images/logo.png"))
                .andExpect(status().isOk())
                .andExpect(cookie().doesNotExist("MySecurityFilter"));
    }

    @Test
    public void testHttpSerity() throws Throwable {
        mvc
                .perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(cookie().value("MySecurityFilter", "Works"));
    }
}

