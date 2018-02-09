# Mithrilfront

Mithril Play front-end service is WAS. This Web application is designed to communcate Mithril Play API server.  
Mithril Play App connects this application first and get authorized token from API server.   
App application requires their own authorized hash token for response from API server.   

>Mithril front-end server Environment

* JDK 1.8
* Spring framework 4.X 
* Spring boot 1.5.6
* Apache Maven
* Redis 3.X
* Maria Database
* Tomcat 8.X (embeded was)

> Core service
* Authorize    

Generate Auth token for communicate with API Server

* API Server proctection 

Every Mithril play app communicate with Mithril Front. Not API server directly.


> Please note that this codebase is a Work In Progress (WIP), and will soon be ready for the alpha release. Hence we won't be accepting any pull requests until that time.
