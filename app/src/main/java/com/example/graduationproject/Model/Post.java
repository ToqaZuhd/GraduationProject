package com.example.graduationproject.Model;



public class Post {
    private String Name;
    private String Date;
    private String text;
    private float review;
    private String imageURL;

    public Post(String name, String date, String text, String imageURL) {
        Name = name;
        Date = date;
        this.text = text;
        this.imageURL = imageURL;
    }


    public Post(String name, String date, String text, float review , String imageURL) {
        Name = name;
        Date = date;
        this.text = text;
        this.imageURL = imageURL;
        this.review=review;
    }

    public Post() {

    }
    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}
