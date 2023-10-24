# Cake manager microservice
This is a microservice application showcasing CRUD operations for managing cakes.

# API Documentation
Api documentation is done with swagger and can be found here **http://localhost:8088/swagger-ui.html#/**
The endpoints are authenticated, so in-memory spring security is used and the default username and password to execute the endpoints in **user1/P@ssw0rd**. The password is configured in application.yml for now, however a DB solution can be done too. 
# End points
### To create a cake ("localhost:8088/api/cakes")
{
"description": "Yummy Red Velvet Cake",
"ingredients": "Sugar, Cocklates, Sprinkles",
"name": "Red velvet Cake",
"price": 22.20
}

### To update a cake ("localhost:8088/api/cakes/{id}")
{
"description": "Yummy Red Velvet Cake with extra sprinkles",
"ingredients": "Sugar, Cocklates, Sprinkles, Eggs",
"name": "Red velvet Cake More Delicious",
"price": 23.20
}
### To get all cake ("localhost:8088/api/cakes")
### To get a cake ("localhost:8088/api/cakes/{id}")
### To delete a cake ("localhost:8088/api/cakes/{id}")


# To run the application
Without docker : **/CakeManagerService/mvn spring-boot:run**

With docker: **/CakeManagerService/docker-compose up --build**

# Testing the application
Both Integration tests and Junit tests will be run as part of the below command.
Integration tests uses test containers with in-memory H2 database which creates few entries into Cake table and does CRUD operations on top of them.

**mvn test**

# Deployment to AWS
Created a terraform script **main.tf** for deploying the service to AWS fargate cluster. Since left the placeholders for ARN, VPC and Subnets as they are private to an account and some info would change when a new aws fargate cluster is provisioned.

