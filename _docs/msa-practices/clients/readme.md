#### 08: emaillist08, maysite08
<pre>
                                   +-----------------------------------+  
                                   |        emaillist08 backend        |                                             +--------------------+
                                   |         (discovery client)        |            +------------------+             |  emaillist service |
                                   |                                   |    fetch   |   eureka server  |   register  |                    |
                                   | 1. eureka client(only fetch)      | &lt;--------- | service registry | &lt;---------&gt; | 1. eureka client   |
                                   |                                   |            +------------------+    fetch    |                    |  
                                   | 2. lb(spring cloud load balancer) |                                             |                    |
  +----------------------+         | 3. rest template(rest API client) | &lt;-----------------------------------------> | 2. rest api server |  
  | emaillist08 frontend | &lt;------ | 4. landing frontend app(react)    |                                             +--------------------+
  |                      |         |                                   |
  | rest api client      | &lt;-----&gt; | 5. rest api server                |                                             
  +----------------------+         +-----------------------------------+                                             

</pre>


#### 09: emaillist09, maysite09
<pre>
                                   +-----------------------------------+  
                                   |        emaillist09 backend        |                                            
  +----------------------+         |                                   | 
  | emaillist09 frontend | &lt;------ | 1. landing frontend app(react)    |      					
  |                      |         |                                   |								
  |                      |         +-----------------------------------+
  |                      |         +-----------------------------------+                                             +--------------------+
  |                      |         |          gateway server           |            +------------------+             |  emaillist service |
  |                      |         |                                   |    fetch   |   eureka server  |   register  |                    |
  |                      |         | 1. eureka client(only fetch)      | &lt;--------- | service registry | &lt;---------&gt; | 1. eureka client   |
  |                      |         |                                   |            +------------------+    fetch    |                    |  
  |                      |         | 2. LB(spring cloud load balancer) |                                             |                    |
  | rest api client      | &lt;-----&gt; | 3. routing                        | &lt;-----------------------------------------&gt; | 2. rest api server |  
  |                      |         |                                   |                                             |                    |
  +----------------------+         +-----------------------------------+                                             +--------------------+
</pre>


