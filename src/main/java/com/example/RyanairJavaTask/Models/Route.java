package com.example.RyanairJavaTask.Models;

public class Route {

    private String airportFrom;
    private String airportTo;
    private boolean connectingAirport;
    private boolean newRoute;
    private boolean seasonalRoute;
    private String operator;
    private String carrierCode;
    private String group;
    private String[] similarArrivalAirportCodes;
    private String[] tags;

    public Route(String airportFrom, String airportTo, boolean connectingAirport, boolean newRoute,
            boolean seasonalRoute, String operator, String group, String[] similarArrivalAirportCodes, String[] tags) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.newRoute = newRoute;
        this.seasonalRoute = seasonalRoute;
        this.operator = operator;
        this.group = group;
        this.similarArrivalAirportCodes = similarArrivalAirportCodes;
        this.tags = tags;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String[] getSimilarArrivalAirportCodes() {
        return similarArrivalAirportCodes;
    }

    public void setSimilarArrivalAirportCodes(String[] similarArrivalAirportCodes) {
        this.similarArrivalAirportCodes = similarArrivalAirportCodes;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public Route(String airportFrom, String airportTo, boolean connectingAirport, boolean newRoute,
            boolean seasonalRoute, String operator, String group) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.newRoute = newRoute;
        this.seasonalRoute = seasonalRoute;
        this.operator = operator;
        this.group = group;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public boolean isConnectingAirport() {
        return connectingAirport;
    }

    public void setConnectingAirport(boolean connectingAirport) {
        this.connectingAirport = connectingAirport;
    }

    public boolean isNewRoute() {
        return newRoute;
    }

    public void setNewRoute(boolean newRoute) {
        this.newRoute = newRoute;
    }

    public boolean isSeasonalRoute() {
        return seasonalRoute;
    }

    public void setSeasonalRoute(boolean seasonalRoute) {
        this.seasonalRoute = seasonalRoute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
