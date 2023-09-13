package com.example.RyanairJavaTask.Models;

import java.util.ArrayList;


public class FlightsResponse {

    private int month;
    private ArrayList<Day> days;

    public FlightsResponse(int month) {
        this.month = month;
        this.days = new ArrayList<>();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

}
