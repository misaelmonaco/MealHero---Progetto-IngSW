package it.unisannio.usermanagementservice.entity;

import jakarta.persistence.*;

@Entity
public class Customer extends Users {

    private int score;

    protected Customer() { super.setRole("CUSTOMER");}

    public Customer(String username, String email, String password) {
        super(username, email, password);
        super.setRole("CUSTOMER");
        this.score = 0;
    }

    public int getId() {
        return super.getId();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return super.getUsername();
    }

    public void setUsername(String username) {
        super.setUsername(username);
    }

    public String getPassword() {
        return super.getPassword();
    }

    public String getRole(){ return super.getRole();}

    public void setPassword(String password) { super.setPassword(password);}

    public String getEmail(){ return super.getEmail();}

    public void setEmail(String email){ super.setEmail(email);}
}
