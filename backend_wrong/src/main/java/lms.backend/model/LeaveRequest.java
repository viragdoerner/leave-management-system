package lms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    public LeaveRequest() {
    }

    public LeaveRequest(@JsonProperty("startDate") Date startDate, @JsonProperty("endDate") Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date arrival) {
        this.startDate = arrival;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date departure) {
        this.endDate = departure;
    }

}

