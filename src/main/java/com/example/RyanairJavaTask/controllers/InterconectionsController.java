package com.example.RyanairJavaTask.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.RyanairJavaTask.Models.Flight;
import com.example.RyanairJavaTask.Models.FlightsResponse;
import com.example.RyanairJavaTask.Models.Route;

@RestController
@RequestMapping()
public class InterconectionsController {

    @GetMapping("/interconnections")
    public String getAvailableInterconections(@RequestParam String
    departure,
    @RequestParam LocalDateTime departureDateTime, @RequestParam String arrival,
    @RequestParam LocalDateTime arrivalDateTime) {

        FlightsController flightsController = new
        FlightsController(departureDateTime.getYear(),
        departureDateTime.getMonth().getValue(), departureDateTime.getDayOfMonth(), departureDateTime.getHour(), departureDateTime.getMinute(), "DUB", "WRO");
        return flightsController.comprobacion;
    }

    @GetMapping("/rutas")
    public String aaa(){
        RestTemplate restTemplate = new RestTemplate();
        String apiRoute = "https://services-api.ryanair.com/views/locate/3/routes";
        String result = restTemplate.getForObject(apiRoute, String.class);
        JSONArray a =new JSONArray(result);
        System.out.println(a.length());
        return a.get(1).toString();
    }

}