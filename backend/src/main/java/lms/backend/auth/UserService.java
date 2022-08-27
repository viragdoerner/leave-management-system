package lms.backend.auth;

import lms.backend.auth.dao.RoleRepository;
import lms.backend.auth.dto.UserDTO;
import lms.backend.dao.UserRepository;
import lms.backend.exception.CustomMessageException;
import lms.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        UserDTO dto = new UserDTO(user.getId(), user.getFullname(), user.getEmail());
        return dto;
    }

    public User updateUser(UserDTO newUser) throws CustomMessageException {
        User oldUser = this.userRepository.findById(newUser.getId()).orElseThrow(() ->
                new CustomMessageException("Nincs ilyen felhasználó"));
        oldUser.setUsername(newUser.getEmail());
        oldUser.setFullname(newUser.getFullname());
        oldUser.setEmail(newUser.getEmail());

        return this.userRepository.save(oldUser);
    }

    public void deleteUserById(long id) throws CustomMessageException{
        userRepository.findById(id).orElseThrow(() -> new CustomMessageException("Nincs felhasználó ilyen id-val!"));

        this.userRepository.deleteById(id);
    }
}
