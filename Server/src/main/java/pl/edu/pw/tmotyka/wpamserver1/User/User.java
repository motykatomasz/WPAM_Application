package pl.edu.pw.tmotyka.wpamserver1.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

@Entity
public class User {

    @Id
    private String username;

    private String password;

    private String address;

    private String contact;

    @OneToMany(mappedBy="author")
    @JsonBackReference(value = "myposts")
    private List<Post> myPosts;

    @OneToMany(mappedBy="other")
    @JsonBackReference(value = "otherposts")
    private List<Post> joinedPosts;

    public User() {
    }

    public User(String username) {

        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String address, String contact) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Post> getMyPosts() {
        return myPosts;
    }

    public void setMyPosts(List<Post> myPosts) {
        this.myPosts = myPosts;
    }

    public List<Post> getJoinedPosts() {
        return joinedPosts;
    }

    public void setJoinedPosts(List<Post> joinedPosts) {
        this.joinedPosts = joinedPosts;
    }

    public Post getPost(int id){

        Post post = new Post();
        for (Post p: myPosts) {
            if(p.getId() == id)
                return p;
        }
        return post;
    }
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", myPosts=" + myPosts +
                ", joinedPosts=" + joinedPosts +
                '}';
    }
}
