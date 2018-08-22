package hr.foi.air.buuterknige;


public class User {

    public String email;
    public String status;

    public User() {

    }

    public User(String email, String status) {
        this.email = email;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }


}
