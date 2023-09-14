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

        // !!hay que hacer un toString que se vea como el resultado
        sb.append("[{\"stops\":0,\"legs\":");
        sb.append(flightsController.getDirectFlights().toString());
        sb.append("},{\"stops\":1,\"legs\":");
        sb.append(flightsController.getInterconnectedFlights().toString());
        // return sb.toString();
        return flightsController.getInterconnectingAirports().toString();
    }
}