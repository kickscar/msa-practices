## Spring Cloud Config Server

#### 1. Spring Cloud Dependency

```XML
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

#### 2. Config Server Annotation
```java
@SpringBootApplication
@EnableConfigServer
public class ServiceConfigServer {

	public static void main(String[] args) {
		SpringApplication.run(ServiceConfigServer.class, args);
	}

}
```

#### 3. Configuration

1. Tomcat Server

```yml
server:
   port: 7777
```

2. file-system based(native)

```yml
spring:
  profiles:
    active: native
  cloud:
     config:
       server:
           native:
              searchLocations: file:///Users/kickscar/DoWork/STS-Workspace/msa-practices-config-repo/service-emaillist
```

3. git based

```yml
spring:
   cloud:
      config:
         server:
            encrypt.enabled: true
            git:
               uri: https://github.com/kickscar/msa-practices-config-repo
               searchPaths: service-emaillist
```

#### 4. Encrypot(암호화)

1. keystore 생성

```sh
keytool -genkeypair -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass password -validity 10000
```

2. classpath에 keystore 파일(keystore.jks) 등록

```
src/main/resources
	|- keystore.jks
	
```

3. Configuration(application.yml) 다음 섹션 추가

```yml
encrypt:
  key-store:
    alias: {alias}
    location: classpath:keystore.jks
    password: {storepass}
```


4. 값 암호화 하기

```sh
curl {service-config-server}/encrypt -d "webdb"
AQC3QkSYiQPShNYRPR7Yqg8DbGvEo2jC+VEf8XL1naBtSvw4YyC5n7JPCaKgqQMgZE9ksm59myUAt5WWtWYENqkQH+J3LjiHurLQXTmOGML58f+Tq6Q=
```

5. 설정에 추가

```yml
   datasource:
      driver-class-name: org.mariadb.jdbc.Driver
      url: jdbc:mysql://{DBServer}/webdb?characterEncoding=utf8
      username: webdb
      password: '{cipher}AQC3QkSYiQPShNYRPR7Yqg8DbGvEo2jC+VEf8XL1naBtSvw4YyC5n7JPCaKgqQMgZE9ksm59myUAt5WWtWYENqkQH+J3LjiHurLQXTmOGML58f+Tq6Q='   
```

6. Endpoit로 확인


	