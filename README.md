# RyanairJavaTask

### Project Overview

This is a Spring Boot based RESTful API application which serves information about possible direct and
interconnected flights (maximum 1 stop) based on the data consumed from external APIs.

### Installation Instructions

To execute the aplication you will need both Java JDK and Maven in your computer.
To download Maven: <a href="https://maven.apache.org/download.cgi"> Download Maven </a>
To download Java JDK: <a href="https://www.oracle.com/es/java/technologies/downloads/"> Download Java JDK </a>

First you will need to build the application using maven, with the following command in the terminal.
{Route to the project}\RyanairJavaTask> mvn clean package

This will create a "target" folder and a .jar executable in it with a name similar to RyanairJavaTask-0.0.1-SNAPSHOT.jar

To run the application, execute the .jar
{Route to the project}\RyanairJavaTask\target> java -jar .\RyanairJavaTask-0.0.1-SNAPSHOT.jar

### Usage Guide

The application responds to following request URI with given query parameters:
http://localhost:8080/flights/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}

For example:
<a href="http://localhost:8080/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2024-03-01T07:00&arrivalDateTime=2024-03-03T21:00">http://localhost:8080/flights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2024-03-01T07:00&arrivalDateTime=2024-03-03T21:00</a>
