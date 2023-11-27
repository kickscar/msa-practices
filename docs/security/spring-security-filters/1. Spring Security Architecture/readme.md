## 1. Spring Security Architecture
<pre>
+---------------------------------------------------------+
|                                                         |          
|  Servlet Context(Tomcat)                                |            +----------------------------------------------------------------------------------------+
|                                                         |            |                                                                                        |
|                                                         |            |  Application Context(Spring Container)                                                 |
|      DelegatingFilterProxy: Filter                      |            |                                                                                        |
|     +---------------------------------------------+     |            |                                                                                        |
|     | urlPattern: "/*"                            |     |            |      FilterChainProxy("springSecurityFilterChain"): Filter                             |
|     | targetBeanName: "springSecurityFilterChain" |     |            |     +---------------------------------------------------------------------------+      |
|     |---------------------------------------------|     |  delegate  |     |                                                                           |      |
|     | doFilter()                                  | ---------------------> | doFilter()                                                                |      |
|     +---------------------------------------------+     |            |     |    |                                                                      |      |
|                                                         |            |     |    |-> SecurityFilterChain1                                               |      |
|                                                         |            |     |    |   "/assets/**": []                                                   |      |
+---------------------------------------------------------+            |     |    |                                                                      |      |
                                                                       |     |    |-> SecurityFilterChain2                                               |      |
                                                                       |     |        "/**": [SecurityFilter1, SecurittFilter2, SecurityFilter3, ...]    |      |     
                                                                       |     |                                                                           |      |     
                                                                       |     +---------------------------------------------------------------------------+      |
                                                                       |                                                                                        |
                                                                       |                                                                                        |
                                                                       +----------------------------------------------------------------------------------------+
</pre>


#### 예제 01
1. FilterChainProxy Bean 명시적 생성
2. SecurityFilterChain 직접 구현
3. Security Configuration
	me.kickscar.spring.security.config.explicit.SecurityConfig01
4. test
	me.kickscar.spring.security.config.explicit.SecurityConfig01Test  
	
#### 예제 02
1. FilterChainProxy Bean 명시적 생성
2. Spring Security에서 제공하는 DefaultSecurityFilterChain 사용
3. Security Configuration
	me.kickscar.spring.security.config.explicit.SecurityConfig02
4. test
	me.kickscar.spring.security.config.explicit.SecurityConfig02Test 