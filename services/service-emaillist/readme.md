## Spring Cloud Discovery Client: Neflix Eureka


#### 1. Spring Cloud Dependency

```XML
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 2. Configuration

1. 서비스 이름

```yml
spring:
   application:
      name: service-emaillist
   profiles:
      active: development
```

- 서비스 등록 이름으로 eureka 서버에 서비스를 질의할 이름이다.
- active 프로파일을 development로 설정 하였다.


2. Tomcat Server

```yml
server:
   port: 0
   servlet:
      context-path: /
      encoding:
         charset: UTF-8
         enabled: true
```

- 포트를 지정할 수 있지만 0으로 설정하여 등록과정에서 랜덤한 포트가 자동으로 할당되도록 한다.
- 서비스 포트는 이 서비스의 클라이언트가 직접 알고 있을 필요가 없다.


3. Eureka Client
	
```yml
eureka:
   instance:
      preferIpAddress: true
   client:
      fetchRegistry: true
      registerWithEureka: true
      serviceUrl:
         defaultZone: http://localhost:8761/eureka
```

- eureka.instance.preferIpAddress<br>

- eureka.client.fetchRegistry<br>

- eureka.client.registerWithEureka<br>

- eureka.client.serviceUrl.defaultZone<br>

- 이 설정은 development profile 설정이다. 

	
4. Application

```yml
spring:
   devtools:
      livereload:
         enabled: true
   datasource:
      driver-class-name: org.mariadb.jdbc.Driver
      url: jdbc:mysql://localhost:3306/webdb?characterEncoding=utf8
      username: webdb
      password: webdb   

mybatis:
   config-location: classpath:mybatis/configuration.xml
```

서비스 비즈니스 구현을 위한 데이터베이스 설정이다. 이 설정은 evelopment profile 설정이다.

 

#### 3. 서비스 구성 서버(Service Configuration Server) 운용

단일 서비스의 여러 인스턴스들의 공통 설정을 중앙에서 관리하기 위해, 서비스 구성 서버(Service Configuration Server)를 운용할 수 있다. 이 때는 설정 내용을 구성 서버로 부터 버시스 시작 전에 받아야 한다. 다음의 의존성과 추가 설정이 필요하다.

1. Spring Cloud Dependency

```XML
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

2. Configuration

```yml
spring:
   config:
      import: optional:configserver:http://localhost:7777
```

development profile 설정이다.



#### 4. 서비스 등록 확인

1.	서비스 실행 후, Eureka 서버 Dashboard에서 서비스 등록 상태를 확인 할 수 있다.
2.	접근<br>
	http://EurekaServer:8761

	