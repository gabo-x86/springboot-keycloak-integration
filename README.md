# Spring Boot  integrated with Keycloak
## Summary
This repository contains the backend of an application developed with Spring Boot and integrated with Keycloak.

The project needs java 21 to be run and it works on port 8081 by default.

The application provides the following endpoints to Create, Read, Update, and Delete users using Keycloak as a service that perform the actions:

| Endpoint | Description |
| ------ | ------ |
| /keycloak/user/search | Get all the registered users |
| /keycloak/user/search/{username} | Search an specific user |
| /keycloak/user/create | Creates a new user |
| /keycloak/user/update/{userId} | Updates the user data given a user id |
| /keycloak/user/delete/{userId} | Deletes a user given a user id |

To run the project, just import the project to IntelliJ IDE and run. That will create a local server accessible at the URL http://localhost:8081
