# Holistic FaaS

Holistic FaaS is a tool to deploy and manage serverless applications around different cloud providers.

### Structure

1. [Management System](holistic-faas-management-system/README.md)
2. [Sample Generator](holistic-faas-sample-generator)
3. [User Interface](holistic-faas-ui)

### Urls for local development

- [Server](http://localhost:8080/hf)
- [Swagger](http://localhost:8080/hf/swagger-ui/#/)
- [Client](http://localhost:8081)

### Usage of the application:

- [How to use the application](doc/GettingStarted.md)

### Installation of the application with docker

- Download the [docker-compose.yml](./docker-compose.yml)
- `cd` into the folder with the docker-compose.yml
- Pull the images with:

```shell
docker-compose pull
```

- Run the application with:

```shell
docker-compose up -d
```

- You should now be able to access the application at [http://localhost:80](http://localhost:80)