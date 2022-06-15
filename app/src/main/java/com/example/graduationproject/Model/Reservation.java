package com.example.graduationproject.Model;

import java.util.Date;

public class Reservation {
    int carID;
    String carType;
    String reservationStartDate;
    String reservationEndDate;
    String PassengerName;
    String PassengerPhoneNumber;


    public Reservation() {
    }

    public Reservation(int carID, String carType, String reservationStartDate, String reservationEndDate, String passengerName, String passengerPhoneNumber) {
        this.carID = carID;
        this.carType = carType;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        PassengerName = passengerName;
        PassengerPhoneNumber = passengerPhoneNumber;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getReservationStartDate() {
        return reservationStartDate;
    }

    public void setReservationStartDate(String reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    public String getReservationEndDate() {
        return reservationEndDate;
    }

    public void setReservationEndDate(String reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    public String getPassengerName() {
        return PassengerName;
    }

    public void setPassengerName(String passengerName) {
        PassengerName = passengerName;
    }

    public String getPassengerPhoneNumber() {
        return PassengerPhoneNumber;
    }

    public void setPassengerPhoneNumber(String passengerPhoneNumber) {
        PassengerPhoneNumber = passengerPhoneNumber;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "carID=" + carID +
                ", carType='" + carType + '\'' +
                ", reservationStartDate='" + reservationStartDate + '\'' +
                ", reservationEndDate='" + reservationEndDate + '\'' +
                ", PassengerName='" + PassengerName + '\'' +
                ", PassengerPhoneNumber='" + PassengerPhoneNumber + '\'' +
                '}';
    }
}
