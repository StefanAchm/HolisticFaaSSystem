# Holistic FaaS

Holistic FaaS is a tool to deploy and manage serverless applications around different cloud providers.

### Structure

1. [Management System](holistic-faas-management-system/README.md)
2. [Sample Generator](holistic-faas-sample-generator)
3. [User Interface](holistic-faas-ui)

### Urls

- [Server](http://localhost:8080/hf)
- [Swagger](http://localhost:8080/hf/swagger-ui/#/)
- [Client](http://localhost:8081)

### Other links:

- [How to use the application](doc/GettingStarted.md)

### Installation of docker images

- Download the artifact from the github page
- Unzip the artifact
- Cd into the folder
- Load the images with:
  - `docker load -i holistic-faas-db.tar`
  - `docker load -i holistic-faas-management-system.tar`
  - `docker load -i holistic-faas-ui.tar`
- Run the docker compose file with:
  - `docker-compose up`
- You should now be able to access the application at [http://localhost:8081](http://localhost:8081)