package com.example.competition;

public class Athlete {
    private String fullName;
    private String gender;
    private String distance;
    private int timeInSeconds;

    // Геттеры и сеттеры
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getDistance() { return distance; }
    public void setDistance(String distance) { this.distance = distance; }
    public int getTimeInSeconds() { return timeInSeconds; }
    public void setTimeInSeconds(int timeInSeconds) { this.timeInSeconds = timeInSeconds; }
}
