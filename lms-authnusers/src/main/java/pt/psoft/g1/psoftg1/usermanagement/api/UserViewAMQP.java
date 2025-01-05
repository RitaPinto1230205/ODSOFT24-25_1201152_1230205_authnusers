package pt.psoft.g1.psoftg1.usermanagement.api;

public class UserViewAMQP {
    private String username;
    private String name;
    private String email;

    // getters e setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}