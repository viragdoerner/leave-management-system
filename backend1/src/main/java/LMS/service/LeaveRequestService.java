package LMS.service;

import LMS.dao.LeaveRequestRepository;
import LMS.model.LeaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LeaveRequestService {

    private LeaveRequestRepository leaveRepository;

    @Autowired
    LeaveRequestService(LeaveRequestRepository lrr){
        this.leaveRepository = lrr;
    }

    public List<LeaveRequest> getAllLeave() {
        List<LeaveRequest> list = leaveRepository.findAll();
        return list;
    }

    @Transactional
    public LeaveRequest addLeave(LeaveRequest leave) {
        LeaveRequest saved_leave = leaveRepository.save(leave);
        return saved_leave;
    }
}
