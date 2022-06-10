package com.example.graduationproject.Model;

public class PostEmployee {
    private int id;
    private String date;
    private String text;

    public PostEmployee(int id, String date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PostEmployee{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
