package me.kickscar.spring.security.config.explicit;

import me.kickscar.spring.security.filter.MySecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig01 {
    @Bean
    public FilterChainProxy springSecurityFilterChain() {
        List<SecurityFilterChain> SecurityFilterChains = Arrays.asList(
                new SecurityFilterChain() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        String uri = request.getRequestURI().replaceAll(request.getContextPath(), "");
                        return  new AntPathMatcher().match("/assets/**", uri);
                    }
                    @Override
                    public List<Filter> getFilters() {
                        return null;
                    }
                },
                new SecurityFilterChain() {
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        String uri = request.getRequestURI().replaceAll(request.getContextPath(), "");
                        return new AntPathMatcher().match("/**", uri);
                    }

                    @Override
                    public List<Filter> getFilters() {
                        return Arrays.asList(
                                new DisableEncodeUrlFilter(),
                                new WebAsyncManagerIntegrationFilter(),
                                new SecurityContextPersistenceFilter(),
                                new MySecurityFilter()
                        );
                    }
                }
        );

        return new FilterChainProxy(SecurityFilterChains);
    }
}
