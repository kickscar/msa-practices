package me.kickscar.spring.security.config.explicit;

import me.kickscar.spring.security.filter.MySecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig02 {
    @Bean
    public FilterChainProxy springSecurityFilterChain() {
        List<SecurityFilterChain> SecurityFilterChains = Arrays.asList(
                new DefaultSecurityFilterChain(new AntPathRequestMatcher("/assets/**")),
                new DefaultSecurityFilterChain(new AntPathRequestMatcher("/**"),
                        new DisableEncodeUrlFilter(),
                        new WebAsyncManagerIntegrationFilter(),
                        new SecurityContextPersistenceFilter(),
                        new MySecurityFilter()
                )
        );

        return new FilterChainProxy(SecurityFilterChains);
    }
}