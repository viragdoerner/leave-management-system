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
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        UserDTO dto = new UserDTO(user.getId(), user.getFullname(), user.getEmail(), user.getHolidays(), user.getSaturday());
        return dto;
    }

    public User updateAnyUser(UserDTO newUser) throws CustomMessageException {
        User oldUser = this.userRepository.findById(newUser.getId()).orElseThrow(() ->
                new CustomMessageException("Nincs ilyen felhasználó"));
        oldUser.setUsername(newUser.getEmail());
        oldUser.setFullname(newUser.getFullname());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setHolidays(newUser.getHolidays());
        oldUser.setSaturday(newUser.getSaturday());

        return this.userRepository.save(oldUser);
    }

    public User updateUser(UserDTO newUser) throws CustomMessageException {
        User oldUser =  getCurrentFullUser();
        oldUser.setUsername(newUser.getEmail());
        oldUser.setFullname(newUser.getFullname());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setHolidays(newUser.getHolidays());
        oldUser.setSaturday(newUser.getSaturday());

        return this.userRepository.save(oldUser);
    }

    public void deleteUserById(long id) throws CustomMessageException{

        User user = userRepository.findById(id).orElseThrow(() -> new CustomMessageException("Nincs felhasználó ilyen id-val!"));
        Role isAdmin = user.getRoles().stream().filter(x -> x.getName().equals(RoleName.ROLE_ADMIN)).findFirst().orElse(null);

        if (isAdmin != null) {
            List<User> admins = userRepository.findAllAdmin(RoleName.ROLE_ADMIN);
            if (admins.size() == 1) {
                throw new CustomMessageException("Nem lehet kitörölni az utolsó admint!");
            }
        }

        user.setRoles(null);
        this.userRepository.saveAndFlush(user);
        this.userRepository.deleteById(id);
    }

    public UserDTO createUser(RegisterForm signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getEmail())) {
            throw new CustomMessageException("Email is already in use!");
        }

        // Creating user's account
        User user = new User(signUpRequest.getFullname(), signUpRequest.getEmail(),signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getHolidays(), signUpRequest.getSaturday());

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

    public User getCurrentFullUser() {
        String currentUsername = getCurrentUsername();
        User user = userRepository.findByEmail(currentUsername).orElseThrow(() -> new RuntimeException("No user has been found with given email"));
        return user;
    }

    String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        throw new CustomMessageException("Nincs bejelentkezett felhasználó!");
    }

    public void updateUserPassword(String password, String oldPassword) {
        User currentUser = this.getCurrentFullUser();
        if (!checkIfValidOldPassword(currentUser, oldPassword)) {
            throw new CustomMessageException("Wrong password!");
        }
        currentUser.setPassword(encoder.encode(password));
    }

    private boolean checkIfValidOldPassword(User currentUser, String oldPassword) {
        return currentUser.getPassword() == encoder.encode(oldPassword);
    }

    public void updateAnyUserPassword(String password, String oldPassword, Long id) {
        User currentUser = this.getCurrentFullUser();
        if (!checkIfValidOldPassword(currentUser, oldPassword)) {
            throw new CustomMessageException("Wrong password!");
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("No user has been found with given id"));
        user.setPassword(encoder.encode(password));
    }
}
