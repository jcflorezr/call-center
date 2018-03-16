# Call Center

Dummy Object Oriented Programming project for simulating a call center module, where there is a chain of responsibility composed by 
hierarchy levels of employees that will answer the "incoming calls". 
#### Release 0.1
##### How to execute it: Through the test cases specified in the unit test classes inside the [call-center test package](https://github.com/jcflorezr/call-center/tree/release-0.1/src/test/java/net/learningpath/callcenter).
* If there are no available employees, an unavailability notification will be sent to the dispatcher and the calls will be queued.
* Once an employee is available again, the dispatcher will be notified and it will proceed to dispatch a previously enqueued call.
#### Release 1.0
##### How to execute it: Through the test cases specified in the unit test classes inside the [call-center test package](https://github.com/jcflorezr/call-center/tree/release-1.0/src/test/java/net/learningpath/callcenter). Although [ControllerTest](https://github.com/jcflorezr/call-center/blob/release-1.0/src/test/java/net/learningpath/callcenter/ControllerTest.java) acts as Integration Test class, providing a test case that executes the entire module in parallel mode for testing concurrency.
* If there are no available employees, ~~an unavailability notification will be sent to the dispatcher and the calls will be queued~~
the current dispatcher which is in charge of assigning the current call to an employee will be enqueued and put on hold.
* Once an employee is available again, ~~the dispatcher will be notified and it will proceed to dispatch a previously enqueued call~~
a dispatcher, which is holding the pending call, will be dequeued and notified immediately so that it can proceed to reach the available employee and assign it to the pending call.
#### Release 1.1
##### How to execute it: Same as release 1.0
* Minor release. Only adding Spring support for delegating the configuration of the chain of responsibility to a Spring container in order to separate the configuration from the implementation
#### Release 2.0
##### How to execute it: Through the test case specified in the integration test class [ControllerIT.java](https://github.com/jcflorezr/call-center/tree/release-2.0/src/test/java/net/learningpath/callcenter/controller/ControllerIT.java).
* The application has become in a web application providing a REST API which is [CallCenterControllerImpl.java](https://github.com/jcflorezr/call-center/tree/release-2.0/src/main/java/net/learningpath/callcenter/controller/CallCenterControllerImpl.java) and its endpoint is "/v1/call-center/calls/{clientName}".


## RELEASE 0.1

### Description
In this release the project is implemented with the Java SE platform only. 
#### The inversion of control is implemented through:
* The strategy pattern provided by the family of classes created implementing the Abstract Factory pattern inside the 
[net.learningpath.callcenter.employee](https://github.com/jcflorezr/call-center/tree/release-0.1/src/main/java/net/learningpath/callcenter/employee)
module.
* The freedom of choice how the chain of responsibility will be composed from the client side (look the [DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java)
class constructor).
#### In order to make a concurrent call center module capable of attending multiple calls in parallel:
* It was created a topic ([EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/event/topic/EmployeesAvailability.java))
which is in charge to notify the suscribers ([DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java))
for both availability and unavailability of employees to attend the calls.

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming in java
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)


## RELEASE 1.0

### Description
In this release the project is implemented with the Java SE platform only. 
#### The inversion of control is implemented through:
* The strategy pattern provided by the family of classes created implementing the Abstract Factory pattern inside the 
[net.learningpath.callcenter.employee](https://github.com/jcflorezr/call-center/tree/release-1.0/src/main/java/net/learningpath/callcenter/employee)
module.
* The freedom of choice how the chain of responsibility will be composed from the client side (look the ~~[DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java)~~
[Controller](https://github.com/jcflorezr/call-center/blob/release-1.0/src/main/java/net/learningpath/callcenter/Controller.java) class constructor).
#### In order to make a concurrent call center module capable of attending multiple calls in parallel:
* ~~It was created a topic ([EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/event/topic/EmployeesAvailability.java)) which is in charge to notify the suscribers ([DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java))
for both availability and unavailability of employees to attend the calls.~~ The Producer-Consumer pattern has been implemented to put on hold the disposed dispatchers and release them when there is employees availability again. Inside [DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-1.0/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java) we will find the Consumer logic and inside [EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/release-1.0/src/main/java/net/learningpath/callcenter/event/EmployeesAvailability.java) we will find the Producer logic.

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming in java
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)


## RELEASE 1.1

### Description
Minor release. Only adding spring support for delegating the configuration of the chain of responsibility to a Spring container in order to separate the configuration from the implementation

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming in java
* [Spring Core](https://spring.io/)
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)


## RELEASE 2.0

### Description
In this release the project has become in a web application and it is implemented with the Java SE platform along with Spring. 
#### The inversion of control is implemented through:
* The strategy pattern provided by the family of classes created implementing the Abstract Factory pattern inside the 
[net.learningpath.callcenter.employee](https://github.com/jcflorezr/call-center/tree/release-2.0/src/main/java/net/learningpath/callcenter/employee)
module.
* ~~The freedom of choice how the chain of responsibility will be composed from the client side (look the [DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java)~~
~~[Controller](https://github.com/jcflorezr/call-center/blob/release-1.0/src/main/java/net/learningpath/callcenter/Controller.java) class constructor).~~ Spring has been introduced for managing the creation of the chain of responsibility among the employee hierarchy levels (look these config classes: [CallCenterControllerConfig](https://github.com/jcflorezr/call-center/tree/release-2.0/src/main/java/net/learningpath/callcenter/config/root/CallCenterControllerConfig.java), [EmployeesHierarchyConfig](https://github.com/jcflorezr/call-center/tree/release-2.0/src/main/java/net/learningpath/callcenter/config/root/EmployeesHierarchyConfig.java))
#### In order to make a concurrent call center module capable of attending multiple calls in parallel:
* ~~It was created a topic ([EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/event/topic/EmployeesAvailability.java)) which is in charge to notify the suscribers ([DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-0.1/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java))
for both availability and unavailability of employees to attend the calls.~~ The Producer-Consumer pattern has been implemented to put on hold the disposed dispatchers and release them when there is employees availability again. Inside [DispatcherImpl](https://github.com/jcflorezr/call-center/blob/release-2.0/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java) we will find the Consumer logic and inside [EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/release-1.0/src/main/java/net/learningpath/callcenter/event/EmployeesAvailability.java) we will find the Producer logic.

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming in java
#### Dependency Injection framework
* [Spring Core](https://projects.spring.io/spring-framework/)
#### Web Framework
* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
* [Spring Test](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html)
#### Integration testing
* [REST-Assured API](http://rest-assured.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)
#### Documentation
* [Swagger](https://swagger.io/) - For documenting REST APIs
