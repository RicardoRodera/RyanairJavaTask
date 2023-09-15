package com.example.RyanairJavaTask.controllers;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
public class InterconnectionsController {

    @GetMapping("/interconnections")
    public String getAvailableInterconnections(@RequestParam String departure,
            @RequestParam LocalDateTime departureDateTime, @RequestParam String arrival,
            @RequestParam LocalDateTime arrivalDateTime) {

        FlightsController flightsController = new FlightsController(departure, departureDateTime, arrival,
                arrivalDateTime);

        StringBuilder sb = new StringBuilder();
        if (departureDateTime.isBefore(arrivalDateTime)) {
            sb.append("[{\"stops\":0,\"legs\":");
            //First get the direct flights if any
            sb.append(flightsController.getDirectFlights().toString());
            sb.append("},{\"stops\":1,\"legs\":");
            //Then get the interconnected flights if any
            sb.append(flightsController.getInterconnectedFlights().toString());
            sb.append("}]");
        } else {
            sb.append("WARNING: PLEASE CHANGE THE GIVEN DEPARTURE AND/OR ARRIVAL DATES");
        }
        try {
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR while conerting from StringBuilder to String";
        }
    }
}