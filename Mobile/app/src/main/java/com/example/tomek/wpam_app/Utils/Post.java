package com.example.tomek.wpam_app.Utils;

import java.io.Serializable;

/**
 * Created by Tomek on 10.04.2018.
 */

public class Post implements Serializable{

    private Integer id;

    private String level;

    private String address;

    private String date;

    private Double price;

    private String contact;

    private String author;

    private String other;

    private String other_contact;

    public Post(Integer id, String level, String address, Double price, String contact) {
        this.id = id;
        this.level = level;
        this.address = address;
        this.price = price;
        this.contact = contact;
    }

    public Post(Integer id, String level, String address, String date, Double price, String contact, String author, String other) {
        this.id = id;
        this.level = level;
        this.address = address;
        this.date = date;
        this.price = price;
        this.contact = contact;
        this.author = author;
        this.other = other;
    }

    public Post(Integer id, String level, String address, String date, Double price, String contact, String author, String other, String other_contact) {
        this.id = id;
        this.level = level;
        this.address = address;
        this.date = date;
        this.price = price;
        this.contact = contact;
        this.author = author;
        this.other = other;
        this.other_contact = other_contact;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOther_contact() {
        return other_contact;
    }

    public void setOther_contact(String other_contact) {
        this.other_contact = other_contact;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", level='" + level + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", contact='" + contact + '\'' +
                '}';
    }


}
