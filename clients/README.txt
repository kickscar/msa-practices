
1. 08: emaillist08, maysite08


                                   +-----------------------------------+  
                                   |        emaillist08 backend        |                                             +--------------------+
                                   |         (discovery client)        |            +------------------+             |  emaillist service |
                                   |                                   |    fetch   |   eureka server  |   register  |                    |
                                   | 1. eureka client(only fetch)      | <--------- | service registry | <---------> | 1. eureka client   |
                                   |                                   |            +------------------+    fetch    |                    |  
                                   | 2. lb(spring cloud load balancer) |                                             |                    |
  +----------------------+         | 3. rest template(rest API client) | <-----------------------------------------> | 2. rest api server |  
  | emaillist08 frontend | <------ | 4. landing frontend app(react)    |                                             +--------------------+
  |                      |         |                                   |
  | rest api client      | <-----> | 5. rest api server                |                                             
  +----------------------+         +-----------------------------------+                                             



                                                      

2. 09: emaillist09, maysite09


                                   +-----------------------------------+  
                                   |        emaillist09 backend        |                                            
  +----------------------+         |                                   | 
  | emaillist09 frontend | <------ | 1. landing frontend app(react)    |      					
  |                      |         |                                   |								
  |                      |         +-----------------------------------+
  |                      |         +-----------------------------------+                                             +--------------------+
  |                      |         |          gateway server           |            +------------------+             |  emaillist service |
  |                      |         |                                   |    fetch   |   eureka server  |   register  |                    |
  |                      |         | 1. eureka client(only fetch)      | <--------- | service registry | <---------> | 1. eureka client   |
  |                      |         |                                   |            +------------------+    fetch    |                    |  
  |                      |         | 2. LB(spring cloud load balancer) |                                             |                    |
  | rest api client      | <-----> | 3. routing                        | <-----------------------------------------> | 2. rest api server |  
  |                      |         |                                   |                                             |                    |
  +----------------------+         +-----------------------------------+                                             +--------------------+
 