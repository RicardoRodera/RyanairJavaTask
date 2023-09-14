package com.example.RyanairJavaTask.controllers;

import java.time.LocalDate;
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

        // Period period = Period.between(departureDateTime.toLocalDate(),
        // arrivalDateTime.toLocalDate());

        // Check if there are intermediate airports for a "one stop" route;
        this.interconnectingAirports = getInterconnectingAirports(this.departure, this.arrival);

        if (departureDateTime.getYear() != arrivalDateTime.getYear()) {
            for (int year = departureDateTime.getYear(); year <= arrivalDateTime.getYear(); year++) {
                if (year == departureDateTime.getYear()) {
                    for (int month = departureDateTime.getMonthValue(); month <= Month.DECEMBER.getValue(); month++) {
                        addDirectFlightsOfYearAndMonth(departure, arrival, year, month);
                    }
                } else {
                    if (year == arrivalDateTime.getYear()) {
                        for (int month = Month.JANUARY.getValue(); month <= arrivalDateTime.getMonthValue(); month++) {
                            addDirectFlightsOfYearAndMonth(departure, arrival, year, month);
                        }
                    } else {
                        for (int month = Month.JANUARY.getValue(); month <= Month.DECEMBER.getValue(); month++) {
                            addDirectFlightsOfYearAndMonth(departure, arrival, year, month);
                        }
                    }
                }
            }
        } else {
            for (int month = departureDateTime.getMonthValue(); month <= arrivalDateTime.getMonthValue(); month++) {
                addDirectFlightsOfYearAndMonth(departure, arrival, departureDateTime.getYear(), month);
            }
        }
    }

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

    private void addDirectFlightsOfYearAndMonth(String departure, String arrival, int year, int month) {

        String apiRoute = "";

        apiRoute = generateApiRoute(departure, arrival, year,
                month);

        // Call the api and store the flights locally
        String response = restTemplate.getForObject(apiRoute, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonDays = (JSONArray) (jsonResponse.get("days"));
        if (!jsonDays.isEmpty()) {

            for (Object day : jsonDays) {
                if (day instanceof JSONObject) {
                    JSONObject dayObject = (JSONObject) day;
                    // generar la fecha para comparar
                    LocalDateTime date = generateDayDate(year, month, dayObject.getInt("day"), "23:59");
                    if ((date.isEqual(departureDateTime) || date.isAfter(departureDateTime))
                            && (date.isEqual(arrivalDateTime) || date.isBefore(arrivalDateTime))) {

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
                                    directFlights.add(new Flight(departure, arrival, flightDepartureDateTime,
                                            flightArrivalDateTime));
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // !! NO HAY VUELOS
        }
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
