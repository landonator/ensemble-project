Orlando Melendez's code sample submission for Ensemble

The following project aims to meet the following requirements:
Project - Backend
Implement an API to manage, search and like/dislike movies. The backend can be implemented in any programming language and use any frameworks.
Technical Requirements
• REST or GraphQL
• Support to create, read, update, and delete movies
• Support to search movies by title
• Support to anonymously like/dislike a movie
• Each movie supports title, description, release year, duration, rating
• Integration with SQL based database

Build and running requirements:
Java 11
MariaDB 10

The full REST API application can be run by:
1) Create the 'moviedb' database within the DB
2) Update the application.properties file with the username and password of the user to access the DB
3) Build the project: ./gradlew clean build
4) Run the application: java -jar build/libs/ensemble-project.jar
5) Endpoints can be reached at http://localhost:8080/api/v1/movies...