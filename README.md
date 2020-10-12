# LSQ Take Home Exercise

The LSQ take-home exercise is a Spring Boot RESTful web application that exposes endpoints for:
* uploading invoice data in CSV format and loading the data into a MySQL database
* retrieving resources based on the invoice domain as provided above


## Prerequisites

Ensure that Java 8 is installed:
  ```
  $ java -version
  openjdk version "1.8.0_232"
  OpenJDK Runtime Environment (AdoptOpenJDK)(build 1.8.0_232-b09)
  OpenJDK 64-Bit Server VM (AdoptOpenJDK)(build 25.232-b09, mixed mode)
  ```

MySQL needs to be running locally as well:

  ```
  $ mysql -V
   mysql  Ver 8.0.21 for osx10.15 on x86_64 (Homebrew)
  ```
Additionally, the 'lsq' schema needs to exist in the database with the user 'the' defined wit write and read privileges to that schema.

## Installation

The take-home exercise uses Gradle for building and dependency management via a gradle wrapper.

To build:
```bash
$ ./gradlew clean build
```

##Usage

To run the application:

```bash
$ ./gradlew :bootRun 
```

## Technology Stack

* [Spring Boot](https://spring.io/projects/spring-boot) - For the Web/REST App, JPA
* [Gradle](https://gradle.org/) - For dependency management
* [Lombok](https://projectlombok.org/) - Used to generate getters, setters, builders, and loggers
* [MySQL](https://www.mysql.com/) - For the persistence layer
* [Apache Commons](https://commons.apache.org/) - For CSV parsing
* [Docker](https://www.docker.com/) - For container creation and management (though this is TBD)