package it.unisannio.usermanagementservice.entity;

import jakarta.persistence.*;

@Entity
public class Developer extends Users {

    protected Developer() {super.setRole("DEVELOPER");}

    public Developer(String username, String email, String password) {
        super(username, email, password);
        super.setRole("DEVELOPER");
    }

    public int getId() { return super.getId(); }

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

    public String getRole(){ return super.getRole(); }

    public String getEmail(){ return super.getEmail(); }

    public void setEmail(String email){ super.setEmail(email); }
}
