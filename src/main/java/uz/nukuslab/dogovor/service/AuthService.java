package uz.nukuslab.dogovor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.payload.ApiResponse;
import uz.nukuslab.dogovor.payload.LoginDto;
import uz.nukuslab.dogovor.repository.UserRepository;
import uz.nukuslab.dogovor.security.JwtProvider;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername());
            return new ApiResponse("TOken", true, token);
        } catch (Exception e) {
            return new ApiResponse("Parol yamasa login qa`te", false);
        }
    }

}
