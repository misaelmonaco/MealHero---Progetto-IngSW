package it.unisannio.usermanagementservice.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String role;

    protected Users() {}

    public Users(String username, String email, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = "USER";
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() { return role; }

    public String getEmail() { return email; }

    public void setRole(String role) { this.role = role; }

    public void setEmail(String email) { this.email = email; }
}
