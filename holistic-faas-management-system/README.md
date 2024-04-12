
# Holistic FaaS Management System


## Setup

### Postgres

1. [Download](https://www.postgresql.org/download/) and install Postgres
2. Setup with username and password like described in [application.properties](src/main/resources/application.properties)
3. Create a database `hf`


### Server

1. Download and Install [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Download and Install [Gradle](https://gradle.org/install/)
3. Clone the repository
4. Open the project in your favorite IDE (e.g. IntelliJ IDEA, Eclipse, etc.)
5. Run the gradle project with the `bootRun` task
6. The server will start on [http://localhost:8080/hf](http://localhost:8080/hf)
7. Swagger is available at [ttp://localhost:8080/hf/swagger-ui/#/](http://localhost:8080/hf/swagger-ui/#/)
