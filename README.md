# tickets

[![Build Status](https://api.travis-ci.com/dwstanley/tickets.svg?branch=develop)](https://travis-ci.com/dwstanley/tickets)
[![Coverage Status](https://coveralls.io/repos/github/dwstanley/tickets/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/tickets?branch=develop)

Tickets is a a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

The chosen architecture follows common web service design principles and makes use of **spring-data** and project **lombok**.

The search engine interface and implementation is contained in the `github.dwstanle.tickets.search` package. This decision was made so that a clear separation exists between the standard service logic and the performance critical search functionality. The expectation is that the search capability will be optimized and tweaked over time.

### Build

Tickets uses a standard Maven build file and can be compiled using the following command:

```sh
$ mvn clean package
```

### Test

Tests are written using JUnit 4 and Mockito. There are a combination of unit and integration tests and as such, some of the tests are intended to be run every time the project is built while others should only be run during integration or performance testing before a release. These tests are identified by the `@Category` tag as Fast or Slow but have not yet been configured as separate build targets.

```sh
$ mvn clean test
```

Code coverage is handled by **jacoco** and will be output to `target/jacoco.exec` each time `mvn test` is run. This file is not human readable but can be ingested by tools such as sonar. For convenience this project is configured to use **coveralls** and test results can be found below:

[![Coverage Status](https://coveralls.io/repos/github/dwstanley/tickets/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/tickets?branch=develop)

### Run

```sh
$ mvn clean spring-boot:run
```

The following urls can be used (with limited functionality) to see the application in action. See the 'REST Service' section under considerations for further details.

<http://localhost:8080/tickets/demo>
<http://localhost:8080/tickets/demo/findAndHoldSeats?numSeats="myNumSeats"&customerEmail="myTestEmail">
<http://localhost:8080/tickets/demo/reserveSeats?seatHoldId="mySeatHoldId"&customerEmail="myTestEmail">

### Considerations

The two most important parts of the Tickets service are the 'service' layer and the 'search' engine. Each was designed with its own responsibilities in mind with as much separation of concerns as possible.

The entire application makes use of project **lombok** to reduce boiler plate code in data classes.

#### Reservation Service

The service layer is responsible for handling requests, persisting reservations, and determining when and on what the search engine should be run. It is again divided into service and data access layers using **jpa** and **spring-data**.

#### Ticket Search Engine

The search engine layer is responsible for finding available seats within a provided SeatMap. The search engine was designed to mimic common game solver algorithms with two main components, a Generator (for finding all possible solutions) and an Evaluator (for scoring each solution). Separation of the two allows for an easier to test architecture and the ability to swap in and out implementations of each without changing the behavior of the other.

**Note:** After implementation and some testing it appears that creating **all** possible solutions, while providing the best result, is often less important than quickly finding the first result that meets a set of criteria. A 'break early' condition was added to the interface to allow the evaluator to be run after each result instead of after all possible results. A super efficient algorithm that does not separate these two concerns could implement the TicketSearchEngine interface directly.

#### SeatMaps and Sections

Each Venue consists of one or more Sections (which could be assigned Event specific properties such as price). Each section contains a single SeatMap (which is represented by a string consisting of characters that represent: A - available, X - obstacle, R - reserved, H - held, S - stage).

The concept of sections was omitted from the search engine layer (but included in the service layer) for several reasons. First, this simplifies the responsibilities of the search engine, which already contains complex logic for searching through a multi-dimensional array. Second, it allows the time and memory intensive search algorithm to be parallelized or offloaded in a distributed manor. This also allows the business logic to handle multiple requests for the same event simultaneously by searching different sections to avoid seat assignment conflicts (note: there are arguments for and against this, my implementation does not handle this). Last, it allows some level of oversight into seat assignment by the service layer. Policies such as whether to spread seating evenly across a venue can be implemented and modified without having to re-write the search engine.

This does introduce some limitations on the search engine, most notably the fact that the engine cannot find seat assignments which cross sections. In some scenarios this is acceptable as groups should generally not be sub-divided unless approved by the user (at which point two smaller findSeat queries can be run); however, often times sections are divided by aisles which large groups could be seated across. This issue could be addressed by creating 'virtual' sections at the service layer (that cross aisles) and providing them to the search engine instead.

The naive implementation that was used for sections was for each Venue to have Set of SeatMaps. There is no spatial relationship maintained between sections which limits the ability to implement higher level business rules but also simplifies the logic for generating and evaluating seat requests. Because there is no spatial relationship between the sections the assumption can be made that the each section is defined such that the stage is always in front of the first row.

#### REST Service

A few REST end points have been exposed to help demonstrate the functionality of the application. They are pre-configured to use the the venue specified at startup and do some things, like account creation, automatically for you. The end points and an overview of the demo event can be found at:

<http://localhost:8080/tickets/demo>

Place Requests:
<http://localhost:8080/tickets/demo/findAndHoldSeats?numSeats="myNumSeats"&customerEmail="myTestEmail">
<http://localhost:8080/tickets/demo/reserveSeats?seatHoldId="mySeatHoldId"&customerEmail="myTestEmail">

View Status:
<http://localhost:8080/tickets/demo>
<http://localhost:8080/tickets/demo/numSeatsAvailable>
<http://localhost:8080/tickets/reservations>

A full rest service implementation has not yet been completed but the structure of the model and service classes is consistent with standard web applications and could be easily exposed to support future enhancements.

No GUI was used for development or testing so making use of the exposed endpoints will require knowledge of http GET, JSON, and likely some application such as curl or Postman to send requests.

### Next Steps

Re-evaluate locking and load distribution approach. The current implementation locks search requests by event to prevent multiple users from being assigned the same seats. An alternate approach would be to lock on per section of an event which would allow for higher throughput but would potentially impact the accuracy of 'find best' algorithm and complicate multi-section searches for large requests.
The biggest issue with the current locking approach is that it only supports a single JVM service and does not scale horizontally. A better approach would be to further exam the object model design use database locking.

Add Logging. During development and testing I was able to get by with default logging provided by spring but more custom logging would likely be necessary for a production system.

Known issues with TicketSearchEngine implementation. The existing implementation of the search engine interface will fail to find seats for the following scenarios:
<pre>
X X A A X X     Request 5 seats - Fails because first row is less than minimum per row (3).
X A A A A X                       Should be special cased for odd shaped venues.
R R R R R R
</pre>

<pre>
A A A A         Request 5 seats - Fails because algorithm breaks early on second row when filling from left.
R A A A                           Works with flipped arrangement.
</pre>

Optimize the generation and evaluation pieces of the search engine. Currently implementations rely heavily on early breaking conditions but leave room for improvement. Memory usage and number of iterations through seatmaps should be examined further. Small improvements in these areas maybe have huge impacts on overall performance due to the high volume of requests expected.

Finish implementation of test categories so performance and integration tests are not run every time and can be run on large data sets.