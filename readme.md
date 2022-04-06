### MSA Based on Spring Cloud

1.	견고한 마이크로서비스 구축을 위해서는 다음과 같은 사항을 고려해야 한다.
	- 적정크기
	- 위치 투명성
	- 회복성
	- 반복성
	- 확장성	

2.	마이크로서비스 패턴들은 이러한 요구 조건의 구현 기술들을 분류하고 일반화 한다.
	- 핵심 개발 패턴
		+ 서비스 세분성
		+ 통신 프로토콜
		+ 인터페이스 설계
		+ 서비스간 이벤트 프로세싱

	- 라우팅 패턴
	  + 서비스 디스커버리
	  + 서비스 라우팅(단일 진입점, 게이트웨이)
	  
	- 클라이언트 회복성 패턴
		+ client-side load balance(클라이언트측 부하 분산)
		+ circuit breaker pattern(회로 차단기 패턴)
		+ fallback pattern
		+ bulkhead pattern

	- 보안 패턴
		+ authentification(인증)
		+ authorization(인가)
		+ credential management(자격 증명 관리) & propagation(전파)

	- 로깅 및 추적 패턴
		+ log corelation
		+ log aggregation
		+ microservice tracing

	- 빌드 및 배포 패턴
		+ 빌드 및 배포 파이프라인
		+ infra as code
		+ immutable server
		+ phoenix server

3.	스프링 클라우드(http://projecs.pring.io/spring-cloud) 프로젝트
	- 마이크로서비스 패턴을 구현한 오픈소스 프로젝트들을 스프링 기반에서 통합
	- 마이크로서비스 구축을 위한 다양한 오픈소스 프로젝트들을 스프링 애플리케이션에서 쉽게 설정하고 구성할 수 있다.

4.	마이크로서비스 패턴과 스프링 클라우드 프로젝트
	- Spring Boot: 핵심 개발 패턴*
		+ JSON(통신 프로토콜)*
		+ Spring Rest*

	- Spring Cloud Config: 핵심 개발 패턴(구성관리)*
		+ Git*
		+ Consul
		+ Eureka
		+ Zookeeper

	- Spring Cloud Discovery: 라우팅 패턴(서비스 디스커버리)*
		+ Consul
		+ Eureka*

	- Spring Cloud Hystrix & Ribbon: 클라이언트 회복성 패턴*
		+ Hystrix(circuit breaker, fallback, bulkhead)
		+ Ribbon(load balance)*

	- Spring Cloud Gateway: 라우팅 패턴(단일 진입점)*
		+ Spring Cloud Zuul(Integrated with Netflix Zuul)*
		+ Repalced With Zuul*
	
	- Spring Cloud Stream: 핵심 개발 패턴(서비스간 이벤트 프로세싱)
		+ RabbitMQ
		+ Kafka

	- Spring Cloud Sleuth: 로깅 및 추적 패턴
		+ Pappertrail(log aggregation)
		+ Zipkin(log corelation, microservice tracing)
	
	- Spring Cloud Security: 보안 패턴*
		+ Spring Security(authentification, authorization)*
		+ oAuth2/JWT(자격 증명 관리)*

	- Provisioning: 빌드 및 배포 패턴 
		+ Travis CI(빌드), Jenkins(빌드,배포)*
		+ Docker(배포)
