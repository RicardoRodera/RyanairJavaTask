package com.example.RyanairJavaTask.Models;

import java.util.ArrayList;
import java.util.List;

public class RouteList {
    private List<Route> routes;

    public RouteList(){
        this.routes = new ArrayList<>();
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }


}
