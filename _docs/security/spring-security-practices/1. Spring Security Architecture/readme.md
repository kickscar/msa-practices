## 1. Spring Security Architecture
![c8d352e44262d2618cc7e8ee6a551c3b.png](../../../_resources/c8d352e44262d2618cc7e8ee6a551c3b.png)

#### 예제 01: /security/spring-security-practices
1. FilterChainProxy Bean 명시적 생성
2. SecurityFilterChain 직접 구현
3. Security Configuration
	me.kickscar.spring.security.config.explicit.SecurityConfig01
4. test
	me.kickscar.spring.security.config.explicit.SecurityConfig01Test  
	
#### 예제 02: spring-security-filters
1. FilterChainProxy Bean 명시적 생성
2. Spring Security에서 제공하는 DefaultSecurityFilterChain 사용
3. Security Configuration
	me.kickscar.spring.security.config.explicit.SecurityConfig02
4. test
	me.kickscar.spring.security.config.explicit.SecurityConfig02Test 