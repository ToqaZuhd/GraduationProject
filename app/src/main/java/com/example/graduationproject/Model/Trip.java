package com.example.graduationproject.Model;

import java.util.Date;

public class Trip {
    private int TripID;
    private int PassengerID;
    private int TicketID;
    private int numOfBags;
    private String tripDate;
    private int rewardPoints;


    public Trip() {
    }

    public int getTripID() {
        return TripID;
    }

    public void setTripID(int tripID) {
        TripID = tripID;
    }

    public int getPassengerID() {
        return PassengerID;
    }

    public void setPassengerID(int passengerID) {
        PassengerID = passengerID;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public int getTicketID() {
        return TicketID;
    }

    public void setTicketID(int ticketID) {
        TicketID = ticketID;
    }

    public int getNumOfBags() {
        return numOfBags;
    }

    public void setNumOfBags(int numOfBags) {
        this.numOfBags = numOfBags;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "TripID=" + TripID +
                ", PassengerID=" + PassengerID +
                ", TicketID=" + TicketID +
                ", numOfBags=" + numOfBags +
                ", tripDate='" + tripDate + '\'' +
                ", rewardPoints=" + rewardPoints +
                '}';
    }
}
