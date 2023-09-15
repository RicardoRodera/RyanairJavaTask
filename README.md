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

###App Logic

The application checks every month between the date of departure and the date of arrival to get all flights from and to the designated airports. Then, checks if they aren't too soon or too late and returns them as the possible direct flights.

Then, for the interconnected flights:
1. Gets all routes and checks for routes that have the same departure as the asked flight, stores them, and then other list with all routes with the same arrival. It gets the airports that coincide as arrival of ones and departure of the others as these are the interconnecting airports.

2. For each month of the departure/arrival period, gets all flights form departure to interconnecting airport and looks for the closes available flight from the interconnecting airport to the arrival with departure time at least 2 hour later than its arrival to the interconnection.

3.If any, stores both in a final interconnected flights list which is returned.

That way it gets all flights direct and interconnected(1 stop) from any period of time.
(Please note that for larget intervals of time, the app has to check for exponentially increasing possible interconnected flights)
