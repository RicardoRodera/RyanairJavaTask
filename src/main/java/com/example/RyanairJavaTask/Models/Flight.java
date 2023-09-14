package com.example.RyanairJavaTask.Models;

import java.time.LocalDateTime;

//This class represents a flight 
public class Flight {

    private String departureAirport;
    private String arivalAirport;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    public Flight(String departureAirport, String arivalAirport, LocalDateTime departureDateTime,
            LocalDateTime arrivalDateTime) {
        this.departureAirport = departureAirport;
        this.arivalAirport = arivalAirport;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArivalAirport() {
        return arivalAirport;
    }

    public void setArivalAirport(String arivalAirport) {
        this.arivalAirport = arivalAirport;
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

    @Override
    public String toString() {
        return "{\"departureAirport\":\"" + departureAirport + "\", \"arivalAirport\":\"" + arivalAirport
                + "\", \"departureDateTime\":\"" + departureDateTime + "\", \"arrivalDateTime\":\"" + arrivalDateTime + "\"}";
    }

}