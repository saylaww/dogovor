package uz.nukuslab.dogovor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.Role;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.entity.enums.RoleName;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.UserDto;
import uz.nukuslab.dogovor.repository.RoleRepository;
import uz.nukuslab.dogovor.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse("users list", true, users);
    }

    public ApiResponse getUser(Integer id) {
        Optional<User> byId = userRepository.findById(id);
        if (!byId.isPresent()) {
            return new ApiResponse("Bunday id li user bazada tabilmadi!!!", false);
        }
        User user = byId.get();
        return new ApiResponse("User", true, user);
    }

    public ApiResponse addUser(UserDto userDto) {
        boolean existsByUsername = userRepository.existsByUsername(userDto.getUsername());
        if (existsByUsername) {
            return new ApiResponse("Bunday username li User bazada bar!!!", false);
        }

        Role admin = roleRepository.save(new Role(
                RoleName.ADMIN
        ));

        User user = new User(
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getUsername(),
                passwordEncoder.encode(userDto.getPassword()),
                Collections.singleton(admin)
        );

        userRepository.save(user);

        return new ApiResponse("User added", true);
    }

    public ApiResponse editUser(Integer id, UserDto userDto) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ApiResponse("Bunday id li user bazada tabilmadi!!!", false);
        }
        User user = optionalUser.get();

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return null;
    }

    public ApiResponse deleteUser(Integer id) {
        try {
            userRepository.deleteById(id);
            return new ApiResponse("User deleted!!!", true);
        } catch (Exception e) {
            return new ApiResponse("Error!!!", false);
        }
    }


}
