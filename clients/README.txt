Discover Client 08: emaillist08, maysite08


                                   +-----------------------------------+  
                                   |        emaillist08 backend        |                                             +--------------------+
                                   |         (discovery client)        |            +------------------+             |  emaillist service |
                                   |                                   |    fetch   |   eureka server  |   register  |                    |
                                   | 1. eureka client(only fetch)      | <--------- | service registry | <---------> | 1. eureka client   |
                                   |                                   |            +------------------+    fetch    |                    |  
                                   | 2. LB(spring cloud load balancer) |                                             |                    |
  +----------------------+         | 3. restTemplate(rest API client)  | <-----------------------------------------> | 2. rest api server |  
  | emaillist08 frontend | <------ | 4. landing frontend app(react)    |                                             +--------------------+
  |                      |         |                                   |
  | rest api client      | <-----> | 5. rest api server                |                                             
  +----------------------+         +-----------------------------------+                                             

                                                      
