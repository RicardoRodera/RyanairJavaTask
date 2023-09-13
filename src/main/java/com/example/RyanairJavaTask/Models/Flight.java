package com.example.RyanairJavaTask.Models;

//This class represents a flight 
public class Flight {

    private String departureAirport;
    private String arivalAirport;
    private String departureDateTime;
    private String arrivalDateTime;

   

    public Flight(String departureAirport, String arivalAirport, String departureDateTime, String arrivalDateTime) {
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

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }
}