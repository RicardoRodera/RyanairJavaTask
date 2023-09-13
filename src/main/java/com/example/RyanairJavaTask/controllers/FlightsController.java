package com.example.RyanairJavaTask.controllers;

import java.time.Month;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.client.RestTemplate;

import com.example.RyanairJavaTask.Models.Day;
import com.example.RyanairJavaTask.Models.Flight;
import com.example.RyanairJavaTask.Models.FlightsResponse;

public class FlightsController {

    private ArrayList<Flight> flights = new ArrayList<>();
    public String comprobacion = "";

    private final String API_BASE_ROUTE = "https://services-api.ryanair.com/timtbl/3/schedules/DUB/WRO/";

    public FlightsController(int year, int month, int dayOfMonth, int hour, int minute, String departureAirport,
            String arrivalAirport) {
        // String apiRoute = API_BASE_ROUTE + "years/" + year + "/months/" + month;
        String apiRoute = "https://services-api.ryanair.com/timtbl/3/schedules/DUB/WRO/years/2024/months/3";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiRoute, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonDays = new JSONArray(jsonResponse.get("days").toString());
        if (!jsonDays.isEmpty()) {
            for (Object day : jsonDays) {
                if (day instanceof JSONObject) {
                    JSONObject dayObject = (JSONObject) day;

                    // Check that only direct flights between the day of departure and arrival are
                    // added.

                    // ! Añadir la comprobación de que no sea el dia de llegada o posterior, la hora de llegada es menor si llega el día siguiente !!!! mucho cuidado con esto

                    if (dayObject.getInt("day") >= dayOfMonth) {

                        JSONArray jsonFlights = (JSONArray)dayObject.get("flights");
                        for (Object flight : jsonFlights) {
                            if (flight instanceof JSONObject) {
                                JSONObject flightObject = (JSONObject) flight;
                                
                                // if (checkDepartureTime(hour, minute, flightObject.getString("departureTime")) && checkArrivalTime(hour, minute, flightObject.getString("arrivalTime"))) {

                                    String departureDateTime = year + "-" + month + "-" + dayOfMonth + "T"
                                            + flightObject.getString("departureTime");
                                    String arrivalDateTime = year + "-" + month + "-" + dayOfMonth + "T"
                                            + flightObject.getString("arrivalTime");
                                    flights.add(new Flight(departureAirport, arrivalAirport, departureDateTime,
                                            arrivalDateTime));
                                    comprobacion += ";" + departureDateTime;
                                // }
                            }
                        }
                    }
                }
            }
        } else {
            // No hay vuelos para esos destinos ese mes
        }
    }

    private boolean checkArrivalTime(int scheduledHour, int ScheduledMinute, String arrivalTime) {
        int arrivalHour = Integer.valueOf(arrivalTime.substring(0,2));
        if(arrivalHour > scheduledHour){
            return false;
        } else if (arrivalHour == scheduledHour){
            int arrivalMinute = Integer.valueOf(arrivalTime.substring(3,5));
            if(arrivalMinute >= ScheduledMinute){
                return false;
            }
        } 
        return true;
    }

    private boolean checkDepartureTime(int scheduledHour, int ScheduledMinute, String departureTime) {
        int departureHour = Integer.valueOf(departureTime.substring(0,2));
        if(departureHour > scheduledHour){
            return true;
        } else if (departureHour == scheduledHour){
            int departureMinute = Integer.valueOf(departureTime.substring(3,5));
            if(departureMinute >= ScheduledMinute){
                return true;
            }
        } 
        return false;
    }
}
