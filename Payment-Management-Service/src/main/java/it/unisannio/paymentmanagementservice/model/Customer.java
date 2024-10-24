package it.unisannio.paymentmanagementservice.model;

public class Customer {

    private int id;
    private String username;
    private String password;
    private String email;
    private int score;
    private String role;

    protected Customer() {}

    public Customer(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.role = "CUSTOMER";
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }
}