# Reactive Distributed Sandbox

### Service Flow / Vision Diagram

The goal with this repository is to experiment more with reactive programming using Project Reactor and Spring Boot WebFlux.

![something](assets/ErrorHandlingReference.png?raw=true)
* View the code [here](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/-/blob/v1.0/spring-applications/OrchestrationService/src/main/java/com/rezz/orchestrationservice/controller/UserController.java) to get an explaination of how to simulate the scenarios denoted in the diagram 
* View the excalidraw link [here](https://excalidraw.com/#json=8QppTfwpt-IfSpwi_f6M6,3WypUeHPQD5c4gTwuR3CgQ)

* Files of interest:
  * [FailedTransactionResponse.java](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/-/blob/master/spring-resources/RestCommunications/src/main/java/com/rezz/restcommunications/model/FailedTransactionResponse.java) holds the context for what went wrong during the transaction.
  * [FailedTransactionException.java](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/blob/master/spring-resources/RestCommunications/src/main/java/com/rezz/restcommunications/exception/FailedTransactionException.java) is the exception form of the FailedTransactionResponse.java
    * This is what gets propagated to the [GlobalErrorHandlers](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/blob/master/spring-applications/OrchestrationService/src/main/java/com/rezz/orchestrationservice/handler/GlobalErrorHandler.java) 
  * [WebClientUtils.java](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/blob/master/spring-resources/RestCommunications/src/main/java/com/rezz/restcommunications/webclient/WebClientUtils.java) manages the operations when a RESTful call has an error on the line

### Running this Project locally
* At this time there are no profiles or properties that need to be defined to run this project
* Swagger links for the associated services:
  * Orchestration Service: http://localhost:8080/swagger-ui.html
  * UserService: http://localhost:4203/swagger-ui.html
  * AccountService: http://localhost:4200/swagger-ui.html
  * LoanService: http://localhost:4204/swagger-ui.html


* *NOTE*: AuditService is currently under development and does not need to be running in order to test the code

### Release Notes:
* [v1.0](https://gitlab.prod.fedex.com/2883037/reactive-distributedsandbox/-/tags/v1.0) 
  * Contains the foundation for error handling with HttpStatus codes

### References
* HttpStatus Codes with Explanations [here](https://www.restapitutorial.com/httpstatuscodes.html)
* Mono-repo setup mimicked from [here](https://github.com/2ndPrince/monorepo)
