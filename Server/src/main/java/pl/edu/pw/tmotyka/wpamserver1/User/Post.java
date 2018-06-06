package pl.edu.pw.tmotyka.wpamserver1.User;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue
    private Integer Id;

    @ManyToOne(fetch= FetchType.EAGER)
    private User author;

    @ManyToOne(fetch= FetchType.EAGER)
    private User other;

    private String level;

    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date date;

    private Double price;

    private String contact;


    public Post() {

    }

    public Post(String level, String address, Date date, Double price, String contact) {
        this.level = level;
        this.address = address;
        this.date = date;
        this.price = price;
        this.contact = contact;
    }

    public Post(User author, User other, String level, String address, Date date, Double price, String contact) {
        this.author = author;
        this.other = other;
        this.level = level;
        this.address = address;
        this.date = date;
        this.price = price;
        this.contact = contact;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public User getOther() {
        return other;
    }

    public void setOther(User other) {
        this.other = other;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    @Override
    public String toString() {
        return "Post{" +
                "Id=" + Id +
                ", author='" + author.getUsername() + '\'' +
                ", level='" + level + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", price=" + price +
                ", contact='" + contact + '\'' +
                '}';
    }
}
