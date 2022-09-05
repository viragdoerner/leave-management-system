package lms.backend.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min=3, max = 100)
    private String fullname;


    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    private int holidays;

    @NotBlank
    @JsonProperty("saturday")
    private Boolean saturday;



    public UserDTO() {
    }

    public UserDTO(Long id, String fullname, String email, int holidays, Boolean saturday) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.holidays =holidays;
        this.saturday = saturday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHolidays() {
        return holidays;
    }

    public void setHolidays(int holidays) {
        this.holidays = holidays;
    }

    public Boolean getSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }
}
