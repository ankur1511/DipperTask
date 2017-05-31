# DipperTask

Technical Interview Task for Dipper.

Problem Statement 

Implement a server which should be capable of doing the following:

   - Exposes a GET API as "api/request?connId=19&timeout=80" 
 This API will keep the request running for provided time on the server side. 
 After the successful completion of the provided time it should return {"status":"ok"}

   - Exposes a GET API as "api/serverStatus" 
 This API returns all the running requests on the server with their time left for completion. 
 E.g {"2":"15","8":"10"} where 2 and 8 are the connIds and 15 and 10 is the time remaining for 
 the requests to complete (in seconds).

   - Exposes a PUT API as "api/kill" with payload as {"connId":12} 
This API will finish the running request with provided connId, so that the finished request 
returns {"status":"killed"} and the current request will return {"status":"ok"}. If no running 
request found with the provided connId on the server then the current request should return "status":"invalid connection Id : <connId>"}




For the execution of Code follow the following steps:

1) Clone the repository.
2) Head to the Project directory DipperTask/, write command mvn clean install(If you are using MAC or Linux,
   on windows first you have to install the Maven), this step will build the project and create an executable jar file.
3) Then head to the target/ directory inside the DipperTask/ directory there will be .jar file of the project,
   for executing the Project write command java -jar DipperTask-0.0.1-SNAPSHOT.jar.
4) You can test three API's using CURL or by using POSTMAN by downloading it from here https://www.getpostman.com/apps.

