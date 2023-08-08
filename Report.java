package com.example.incidentreporting;

public class Report {
    private String description;
    private String phoneNumber;
    private String vehicleType;
    private String incidentType;
    private String imageUrl;

    public Report(String description, String phoneNumber, String vehicleType, String incidentType, String imageUrl) {
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.vehicleType = vehicleType;
        this.incidentType = incidentType;
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getIncidentType() {
        return incidentType;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
