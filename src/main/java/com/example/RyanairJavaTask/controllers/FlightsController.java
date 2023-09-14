package com.example.RyanairJavaTask.controllers;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import com.example.RyanairJavaTask.Models.Flight;

public class FlightsController {

    private final String FLIGHTS_API_BASE_ROUTE = "https://services-api.ryanair.com/timtbl/3/schedules/";
    private final String ROUTES_API_ROUTE = "https://services-api.ryanair.com/views/locate/3/routes";
    private ArrayList<Flight> directFlights = new ArrayList<>();
    private ArrayList<Flight> interconnectedFlights = new ArrayList<>();
    private ArrayList<String> interconnectingAirports = new ArrayList<>();
    private String departure;
    private String arrival;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
    private RestTemplate restTemplate = new RestTemplate();

    public String comprobacion = "";

    public FlightsController(String departure, LocalDateTime departureDateTime, String arrival,
            LocalDateTime arrivalDateTime) {

        this.departure = departure;
        this.arrival = arrival;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;

        // Check if there are intermediate airports for a "one stop" route;
        this.interconnectingAirports = getInterconnectingAirports(this.departure, this.arrival);

        if (departureDateTime.getYear() != arrivalDateTime.getYear()) {
            for (int year = departureDateTime.getYear(); year <= arrivalDateTime.getYear(); year++) {
                if (year == departureDateTime.getYear()) {
                    for (int month = departureDateTime.getMonthValue(); month <= Month.DECEMBER.getValue(); month++) {
                        this.directFlights.addAll(getDirectFlightsOfYearAndMonth(departure, arrival, year, month));
                        addInterconnectedFlightsOfYearAndMonth(year,
                                month);
                    }
                } else {
                    if (year == arrivalDateTime.getYear()) {
                        for (int month = Month.JANUARY.getValue(); month <= arrivalDateTime.getMonthValue(); month++) {
                            this.directFlights.addAll(getDirectFlightsOfYearAndMonth(departure, arrival, year, month));
                            addInterconnectedFlightsOfYearAndMonth(
                                    year, month);
                        }
                    } else {
                        for (int month = Month.JANUARY.getValue(); month <= Month.DECEMBER.getValue(); month++) {
                            this.directFlights.addAll(getDirectFlightsOfYearAndMonth(departure, arrival, year, month));
                            addInterconnectedFlightsOfYearAndMonth(
                                    year, month);
                        }
                    }
                }
            }
        } else {
            for (int month = departureDateTime.getMonthValue(); month <= arrivalDateTime.getMonthValue(); month++) {
                this.directFlights
                        .addAll(getDirectFlightsOfYearAndMonth(departure, arrival, departureDateTime.getYear(), month));
                addInterconnectedFlightsOfYearAndMonth(
                        departureDateTime.getYear(), month);
            }
        }
    }

    private void addInterconnectedFlightsOfYearAndMonth(int year, int month) {
        if (!this.interconnectingAirports.isEmpty()) {
            for (String interconnectingAirport : this.interconnectingAirports) {
                ArrayList<Flight> firstFlights = getDirectFlightsOfYearAndMonth(this.departure, interconnectingAirport,
                        year, month);
                // For each flight, it looks if there is one that connects it with the final
                // arrival from the interconnecting airport. If it finds one, it adds both to
                // the final list.
                for (Flight flight : firstFlights) {
                    Flight connectingFlight = searchConnectingFlight(interconnectingAirport, year, month, flight);
                    if (connectingFlight != null) {
                        this.interconnectedFlights.add(flight);
                        this.interconnectedFlights.add(connectingFlight);
                    }
                }

            }
        }
    }

    private Flight searchConnectingFlight(String interconnectingAirport, int year, int month, Flight flight) {
        String apiRoute = "";

        apiRoute = generateApiRoute(interconnectingAirport, this.arrival, year,
                month);

        // Call the api and store the days with flights locally
        String response = restTemplate.getForObject(apiRoute, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonDays = (JSONArray) (jsonResponse.get("days"));
        if (!jsonDays.isEmpty()) {

            for (Object day : jsonDays) {
                if (day instanceof JSONObject) {
                    JSONObject dayObject = (JSONObject) day;
                    // Generate the date of the day and the dateTime of the fist flight +2h to make
                    // comparations
                    LocalDateTime date = generateDayDate(year, month, dayObject.getInt("day"), "23:59");
                    LocalDateTime departureDate = flight.getArrivalDateTime().plusHours(2);
                    if ((date.isEqual(departureDate) || date.isAfter(departureDate))
                            && (date.isEqual(arrivalDateTime) || date.isBefore(arrivalDateTime))) {
                        // Get all flights of the day and check if they are in time for the schedule
                        JSONArray jsonFlights = (JSONArray) dayObject.get("flights");
                        for (Object connectingFlight : jsonFlights) {
                            if (connectingFlight instanceof JSONObject) {
                                JSONObject flightObject = (JSONObject) connectingFlight;

                                LocalDateTime flightDepartureDateTime = generateFlightDateTime(
                                        year, month,
                                        dayObject.getInt("day"), flightObject.getString("departureTime"));

                                LocalDateTime flightArrivalDateTime = generateFlightDateTime(
                                        year, month,
                                        dayObject.getInt("day"), flightObject.getString("arrivalTime"));

                                if ((flightDepartureDateTime.isAfter(departureDate)
                                        || flightDepartureDateTime.isEqual(departureDate))
                                        && (flightArrivalDateTime.isBefore(arrivalDateTime)
                                                || flightArrivalDateTime.isEqual(arrivalDateTime))) {
                                    // Then gets the first flight possible acording to the specifications. Getting
                                    // more than one would be possible but too many options would exist.
                                    return (new Flight(interconnectingAirport, this.arrival, flightDepartureDateTime,
                                            flightArrivalDateTime));

                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    // This method creates two lists, one with the airports connected to the
    // departure and other one with the airports connected to de arrival. Then
    // checks which ones coincide and returns them.
    private ArrayList<String> getInterconnectingAirports(String departure, String arrival) {
        ArrayList<String> possibleInterconnections = new ArrayList<>();
        ArrayList<String> finalInterconnections = new ArrayList<>();

        String response = restTemplate.getForObject(ROUTES_API_ROUTE, String.class);
        JSONArray jsonRoutes = new JSONArray(response);

        for (Object route : jsonRoutes) {
            if (route instanceof JSONObject) {
                JSONObject jsonRoute = (JSONObject) route;

                if (jsonRoute.getString("operator").equals("RYANAIR")
                        && (jsonRoute.getString("airportFrom").equals(departure)
                                || jsonRoute.getString("airportTo").equals(arrival))) {
                    if (jsonRoute.getString("airportFrom").equals(departure)
                            && !jsonRoute.getString("airportTo").equals(arrival)) {

                        if (possibleInterconnections.contains(jsonRoute.getString("airportTo"))) {
                            finalInterconnections.add(jsonRoute.getString("airportTo"));
                        } else {
                            possibleInterconnections.add(jsonRoute.getString("airportTo"));
                        }

                    } else if (!jsonRoute.getString("airportFrom").equals(departure)
                            && jsonRoute.getString("airportTo").equals(arrival)) {
                        if (possibleInterconnections.contains(jsonRoute.getString("airportFrom"))) {
                            finalInterconnections.add(jsonRoute.getString("airportFrom"));
                        } else {
                            possibleInterconnections.add(jsonRoute.getString("airportFrom"));
                        }
                    }
                }
            }
        }
        return finalInterconnections;
    }

    // As the given API to get the flight schedules gives the flights for each
    // month, this method is called once for each month in the period betweeen
    // departure and arrival
    private ArrayList<Flight> getDirectFlightsOfYearAndMonth(String departure, String arrival, int year, int month) {

        ArrayList<Flight> flightList = new ArrayList<>();
        String apiRoute = "";

        apiRoute = generateApiRoute(departure, arrival, year,
                month);

        // Call the api and store the days with flights locally
        String response = restTemplate.getForObject(apiRoute, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonDays = (JSONArray) (jsonResponse.get("days"));
        if (!jsonDays.isEmpty()) {

            for (Object day : jsonDays) {
                if (day instanceof JSONObject) {
                    JSONObject dayObject = (JSONObject) day;
                    // Generate the "present" date to make comparations
                    LocalDateTime date = generateDayDate(year, month, dayObject.getInt("day"), "23:59");
                    if ((date.isEqual(departureDateTime) || date.isAfter(departureDateTime))
                            && (date.isEqual(arrivalDateTime) || date.isBefore(arrivalDateTime))) {
                        // Get all flights of the day and check if they are in time for the schedule
                        JSONArray jsonFlights = (JSONArray) dayObject.get("flights");
                        for (Object flight : jsonFlights) {
                            if (flight instanceof JSONObject) {
                                JSONObject flightObject = (JSONObject) flight;

                                LocalDateTime flightDepartureDateTime = generateFlightDateTime(
                                        year, month,
                                        dayObject.getInt("day"), flightObject.getString("departureTime"));

                                LocalDateTime flightArrivalDateTime = generateFlightDateTime(
                                        year, month,
                                        dayObject.getInt("day"), flightObject.getString("arrivalTime"));

                                if ((flightDepartureDateTime.isAfter(departureDateTime)
                                        || flightDepartureDateTime.isEqual(departureDateTime))
                                        && (flightArrivalDateTime.isBefore(arrivalDateTime)
                                                || flightArrivalDateTime.isEqual(arrivalDateTime))) {
                                    // If they are, they get added to the list of possible flights
                                    flightList.add(new Flight(departure, arrival, flightDepartureDateTime,
                                            flightArrivalDateTime));
                                }
                            }
                        }
                    }
                }
            }
        }
        return flightList;
    }

    private LocalDateTime generateDayDate(int year, int month, int day, String time) {
        StringBuilder sb = new StringBuilder().append(year).append("-");
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month).append("-");
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day).append("T").append(time);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(sb.toString(), dateFormat);
        return dateTime;
    }

    private LocalDateTime generateFlightDateTime(int year, int month, int day, String flightTime) {
        StringBuilder sb = new StringBuilder().append(year).append("-");
        if (month < 10) {
            sb.append("0");
        }
        sb.append(month).append("-");
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day).append("T").append(flightTime);
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime flightDateTime = LocalDateTime.parse(sb.toString(), dateFormat);
        return flightDateTime;
    }

    private String generateApiRoute(String departure, String arrival, int year, int month) {
        StringBuilder sb = new StringBuilder(FLIGHTS_API_BASE_ROUTE);
        sb.append(departure).append("/").append(arrival);
        sb.append("/years/").append(year).append("/months/").append(month);
        return sb.toString();
    }

    public ArrayList<Flight> getDirectFlights() {
        return directFlights;
    }

    public void setDirectFlights(ArrayList<Flight> directFlights) {
        this.directFlights = directFlights;
    }

    public ArrayList<Flight> getInterconnectedFlights() {
        return interconnectedFlights;
    }

    public void setInterconnectedFlights(ArrayList<Flight> interconnectedFlights) {
        this.interconnectedFlights = interconnectedFlights;
    }

    public ArrayList<String> getInterconnectingAirports() {
        return interconnectingAirports;
    }

    public void setInterconnectingAirports(ArrayList<String> interconnectingAirports) {
        this.interconnectingAirports = interconnectingAirports;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
