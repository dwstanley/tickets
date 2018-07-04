# tickets

[![Build Status](https://api.travis-ci.com/dwstanley/tickets.svg?branch=develop)](https://travis-ci.com/dwstanley/tickets)
[![Coverage Status](https://coveralls.io/repos/github/dwstanley/tickets/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/tickets?branch=develop)

Tickets is a a simple ticket service that facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance venue.

The chosen architecture follows common web service design principles and makes use of **spring-data** and project **lombok**.

The search engine interface and implementation is contained in the `github.dwstanle.tickets.search` package. This decision was made so that a clear separation exists between the standard service logic and the performance critical search functionality. The expectation is that the search capability will be optimized and tweaked over time.


### Build
Tickets uses a standard Maven build file and can be compiled using the following command:
```sh
$ mvn clean install

[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building tickets 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------

< Clipped Ouput >

[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 23.883 s
[INFO] Finished at: 2018-07-04T15:26:11-04:00
[INFO] Final Memory: 48M/742M
[INFO] ------------------------------------------------------------------------
```

### Testing
The best way to see the tickets service in action is to run the provided JUnit tests. Some of the tests are intended to be run every time the project is build while other should only be run during integration or performance testing before a release. These tests are identified by the `@Category` tag.

```sh
$ mvn clean test

[INFO] Scanning for projects...
[INFO]                                                                         

< Clipped Ouput >

[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 27, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------

```

Code coverage is handled by **jacoco** and will be output to `target/jacoco.exec` each time `mvn test` is run`. This file is not human readable but can be ingested by tools such as sonar. For convenience this project is configured to use **coveralls** and test results can be found below:
[![Coverage Status](https://coveralls.io/repos/github/dwstanley/tickets/badge.svg?branch=develop)](https://coveralls.io/github/dwstanley/tickets?branch=develop)


### Assumptions