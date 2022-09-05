package lms.backend.auth;

import lms.backend.auth.dao.RoleRepository;
import lms.backend.auth.dto.RegisterForm;
import lms.backend.auth.dto.UserDTO;
import lms.backend.auth.model.Role;
import lms.backend.auth.model.RoleName;
import lms.backend.dao.UserRepository;
import lms.backend.exception.CustomMessageException;
import lms.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public Iterable<UserDTO> getUsers() {
        List<User> all = this.userRepository.findAll();
        List<UserDTO> allDTO = new ArrayList<UserDTO>();
        for (User user : all){
            allDTO.add(this.convertUser(user));
        }
        return allDTO;
    }

    public UserDTO convertUser(User user) {
        UserDTO dto = new UserDTO(user.getId(), user.getFullname(), user.getEmail(), user.getHolidays(), user.isWorksOnSaturday());
        return dto;
    }

    public User updateUser(UserDTO newUser) throws CustomMessageException {
        User oldUser = this.userRepository.findById(newUser.getId()).orElseThrow(() ->
                new CustomMessageException("Nincs ilyen felhasználó"));
        oldUser.setUsername(newUser.getEmail());
        oldUser.setFullname(newUser.getFullname());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setHolidays(newUser.getHolidays());
        oldUser.setWorksOnSaturday(newUser.isWorksOnSaturday());

        return this.userRepository.save(oldUser);
    }

    public void deleteUserById(long id) throws CustomMessageException{
        userRepository.findById(id).orElseThrow(() -> new CustomMessageException("Nincs felhasználó ilyen id-val!"));

        this.userRepository.deleteById(id);
    }

    public UserDTO createUser(RegisterForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            throw new CustomMessageException("Email is already in use!");
        }

        // Creating user's account
        User user = new User(signUpRequest.getFullname(), signUpRequest.getEmail(),signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getHolidays(), signUpRequest.getWorksOnSaturday());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new CustomMessageException("Fail! -> Cause: User Role not found."));
        roles.add(userRole);
        if(signUpRequest.isAdmin()){
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomMessageException("Fail! -> Cause: Admin Role not found."));
            roles.add(adminRole);
        }
        user.setRoles(roles);
        userRepository.save(user);

        return this.convertUser(user);
    }
}
