package lms.backend.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NotBlank
    @JsonProperty("isAdmin")
    private Boolean isAdmin;

    @NotBlank
    private int holidays;

    @NotBlank
    @JsonProperty("worksOnSaturday")
    private Boolean worksOnSaturday;

    public RegisterForm() {
    }


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

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }

    public Boolean getWorksOnSaturday() {
        return worksOnSaturday;
    }

    public void setWorksOnSaturday(Boolean worksOnSaturday) {
        this.worksOnSaturday = worksOnSaturday;
    }
}
