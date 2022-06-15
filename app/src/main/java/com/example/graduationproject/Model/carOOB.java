package com.example.graduationproject.Model;

public class carOOB {
    private int car_number;
    private String carImage;
    private String car_type;
    private int car_price;
    private int seats_number;
    private String gear_type;
    private int providerID;

    public carOOB() {
    }

    public carOOB(int car_number, String carImage, String car_type, int car_price, int seats_number, String gear_type, int providerID) {
        this.car_number = car_number;
        this.carImage = carImage;
        this.car_type = car_type;
        this.car_price = car_price;
        this.seats_number = seats_number;
        this.gear_type = gear_type;
        this.providerID = providerID;
    }

    public int getCar_number() {
        return car_number;
    }

    public void setCar_number(int car_number) {
        this.car_number = car_number;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public int getCar_price() {
        return car_price;
    }

    public void setCar_price(int car_price) {
        this.car_price = car_price;
    }

    public int getSeats_number() {
        return seats_number;
    }

    public void setSeats_number(int seats_number) {
        this.seats_number = seats_number;
    }

    public String getGear_type() {
        return gear_type;
    }

    public void setGear_type(String gear_type) {
        this.gear_type = gear_type;
    }

    public int getProviderID() {
        return providerID;
    }

    public void setProviderID(int providerID) {
        this.providerID = providerID;
    }

    @Override
    public String toString() {
        return "carOOB{" +
                "car_number=" + car_number +
                ", carImage='" + carImage + '\'' +
                ", car_type='" + car_type + '\'' +
                ", car_price=" + car_price +
                ", seats_number=" + seats_number +
                ", gear_type=" + gear_type +
                ", providerID=" + providerID +
                '}';
    }
}
