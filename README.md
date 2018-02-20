# Call Center

* Dummy Object Oriented Programming project for simulating a call center module, where there is a chain of responsibility composed by 
hierarchy levels of employees that will answer the "incoming calls". 
* If there are no available employees, an unavailability notification will be sent to the dispatcher and the calls will be queued.
* Once an employee is available again an availability notification will be sent to the dispatcher an it will dispatch a previously
enqueued call.

## RELEASE 0.1

### Description
In this release the project is implemented with the Java SE platform only. 
#### The inversion of control is implemented through:
* The strategy pattern provided by the family of classes created implementing the Abstract Factory pattern inside the 
[net.learningpath.callcenter.employee](https://github.com/jcflorezr/call-center/tree/feature/v1-call-center-module/src/main/java/net/learningpath/callcenter/employee)
module.
* The freedom of choice how the chain of responsibility will be composed from the client side (look the [DispatcherImpl](https://github.com/jcflorezr/call-center/blob/feature/v1-call-center-module/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java)
class constructor).
#### In order to make a concurrent call center module capable of attending multiple calls in parallel:
* It was created a topic ([EmployeesAvailability](https://github.com/jcflorezr/call-center/blob/feature/v1-call-center-module/src/main/java/net/learningpath/callcenter/event/topic/EmployeesAvailability.java))
which is in charge to notify the suscribers ([DispatcherImpl](https://github.com/jcflorezr/call-center/blob/feature/v1-call-center-module/src/main/java/net/learningpath/callcenter/service/DispatcherImpl.java))
for both availability and unavailability of employees to attend the calls.

### Built with
#### Programming languages
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Vavr (Javaslang)](http://www.vavr.io/) - Library for implementing real functional programming
#### Unit testing
* [JUnit 5](https://junit.org/junit5/)
* [Hamcrest](hamcrest.org)
* [Mockito](http://site.mockito.org/)
* [PowerMock](http://powermock.github.io/)
#### Dependency management
* [Maven](https://maven.apache.org/)
