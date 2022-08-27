package lms.backend.auth.dto;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterForm {
    @NotBlank
    @Size(min=3, max = 60)
    private String email;

    @NotBlank
    @Size(min=3, max = 100)
    private String fullname;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotNull
    private boolean isAdmin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }
}
