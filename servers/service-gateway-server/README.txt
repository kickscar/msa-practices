Spring Cloud Gateway



===
1. Spring Cloud Starter

<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>




===
2. Configuration

1) Tomcat Server


server:
   port: 8888


2) Gateway Management

management:
   endpoint:
      gateway:
         enabled: true    # default value
   endpoints:
      web:
         exposure:
            include: "*"

3) Eureka Client(fetch ant Register)
	
eureka:
   client:
      registerWithEureka: false
      fetchRegistry: true
      serviceUrl:
         defaultZone: http://{serverip}:{serverport}/eureka/


4) Gateway Configuration(Route)

spring:
   cloud:
      gateway:
         globalcors:
            cors-configurations:
               '[/**]':
                  allowedOrigins: '*'
                  allowedHeaders: '*'
                  allow-credentials: true
                  allowedMethods:
                  - GET
                  - POST
                  - PUT
                  - DELETE
                  - OPTIONS                         
         discovery.locator:
            enabled: true
            lowerCaseServiceId: true 
         routes:
         - id: service-emaillist
           uri: lb://service-emaillist
           predicates:
           - Path=/api/emaillist/**
           filters:
           - RewritePath=/api/emaillist/?(?<remaining>.*), /$\{remaining}


===
3. Cors Basics

1) origin(출처)
	
   URL에서 protocol + host + port 를 합친 것
      
    > location.origin
    < 'http://localhost:9999
   
2. 같은 origin VS 다른 origin

3. Same Origin Policy(SOP)

   브라우저가 동일 출처 정책(SOP)를 지켜서 다른 출처의 리소스 접근을 금지

   - 외부 리소스를 못 가져오는 단점
   - XSS 등과 같은 보안 취약점을 사전에 방지할 수 있다.

4. CORS(Cross Orign Resource Sharing) Error
	<p>
	   Access to fetch at 'http://localhost:8888/api' from origin 'http://localhost:9999' has been blocked by CORS policy: Response to preflight request doesn't pass access control check: No 'Access-Control-Allow-Origin' header is present on the requested resource. If an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
	</p>

5. 해결방법 1: simple request


JS                 browser                     server
   ------------->           --------------->
       fetch                GET /api
                                 
                            <---------------
                            200 OK
                            Access-Control-Allow-origin: *
                            

   <-------------
      response                                 

      조건:
      1) GET, HEAD, POST 중의 하나의 method를 쓰는 경우
      2) Accept, Accept-Language, Content-Type 등의 헤더만 사용하는 경우
      3) Content-Type 헤더에 application/x-www-form-urlencoded, multipart/for-data, text/palin 인 경우


6. 해결방법 2: preflight request

JS                 browser                     server
   ------------->           --------------->
       fetch                OPTIONS /api/token
                            Access-Control-Request-Headers: GET

                            <---------------
                            200 OK
                            Access-Control-Allow-Origin: *

                            --------------->
                            GET /api

                            <---------------
                            200 OK
                            Access-Control-Allow-Origin:*
 
   <-------------
      response 

	