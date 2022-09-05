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
    @JsonProperty("worksOnSaturday")
    private Boolean worksOnSaturday;



    public UserDTO() {
    }

    public UserDTO(Long id, String fullname, String email, int holidays, Boolean worksOnSaturday) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.holidays =holidays;
        this.worksOnSaturday = worksOnSaturday;
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

    public Boolean isWorksOnSaturday() {
        return worksOnSaturday;
    }

    public void setWorksOnSaturday(Boolean worksOnSaturday) {
        this.worksOnSaturday = worksOnSaturday;
    }
}
