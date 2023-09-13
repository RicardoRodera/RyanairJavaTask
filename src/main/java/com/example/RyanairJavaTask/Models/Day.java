package com.example.RyanairJavaTask.Models;

import java.util.ArrayList;

public class Day {

    private int day;
    private ArrayList<Flight> flights;

    public Day(int day, ArrayList<Flight> flights) {
        this.day = day;
        this.flights = flights;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
    }

}
