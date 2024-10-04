package it.unisannio.usermanagementservice.entity;

import jakarta.persistence.*;

@Entity
public class Admin extends Users {

    private int restaurantId;

    protected Admin() {super.setRole("ADMIN");}

    public Admin(String username, String email, String password, int restaurantId) {
        super(username, email, password);
        super.setRole("ADMIN");
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return super.getId();
    }

    public int getRestaurantId() { return restaurantId; }

    public void setRestaurantId(int restaurantsId) { this.restaurantId = restaurantsId; }

    public String getPassword() {
        return super.getPassword();
    }

    public void setPassword(String password) {
        super.setPassword(password);
    }

    public String getUsername() {
        return super.getUsername();
    }

    public void setUsername(String username) {
        super.setUsername(username);
    }

    public String getRole() { return super.getRole(); }

    public String getEmail(){ return super.getEmail();}

    public void setEmail(String email){ super.setEmail(email);}
}
