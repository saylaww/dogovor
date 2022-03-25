package uz.nukuslab.dogovor.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.nukuslab.dogovor.entity.Role;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.entity.enums.RoleName;
import uz.nukuslab.dogovor.repository.RoleRepository;
import uz.nukuslab.dogovor.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            Role sAdmin = roleRepository.save(new Role(
                    RoleName.SUPER_ADMIN
            ));

            Role admin = roleRepository.save(new Role(
                    RoleName.ADMIN
            ));

            userRepository.save(new User(
                    "Bill",
                    "Gates",
                    "bill",
                    passwordEncoder.encode("123"),
                    Collections.singleton(sAdmin)
            ));

            userRepository.save(new User(
                    "Stieve",
                    "Jobs",
                    "stiv",
                    passwordEncoder.encode("456"),
                    Collections.singleton(admin)
            ));
        }
    }

}
