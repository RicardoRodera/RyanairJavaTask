package com.example.RyanairJavaTask.controllers;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.example.RyanairJavaTask.Models.Route;
import com.example.RyanairJavaTask.Models.RouteList;

public class RoutesController {

    public List<Route> cosa;

    public RoutesController() {
        RestTemplate restTemplate = new RestTemplate();
        String apiRoute = "https://services-api.ryanair.com/views/locate/3/routes";
        RouteList result = restTemplate.getForObject(apiRoute, RouteList.class);
        cosa = result.getRoutes();
    }

}
