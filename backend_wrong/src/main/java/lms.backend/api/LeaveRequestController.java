package lms.api;

import lms.model.LeaveRequest;
import lms.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RequestMapping("/leave-request")
@RestController
public class LeaveRequestController {


        private final LeaveRequestService leaveService;

        @Autowired
        public LeaveRequestController(LeaveRequestService bookingService) {
            this.leaveService = bookingService;
        }

        @GetMapping
        public List<LeaveRequest> getAllLeave() {
            return leaveService.getAllLeave();
        }

        @PostMapping
        public LeaveRequest addLeave(@RequestBody LeaveRequest leave) {
            return leaveService.addLeave(leave);
        }
    }

