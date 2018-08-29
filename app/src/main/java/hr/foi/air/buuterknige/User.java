package hr.foi.air.buuterknige;


import android.widget.TextView;

import java.lang.ref.SoftReference;

public class User {

    public String email;
    public String username;
    public String status;
    public String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }





    public User() {

    }
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User(String email, String status, String username) {
        this.email = email;
        this.status = status;
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
