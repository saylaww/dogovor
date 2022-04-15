package uz.nukuslab.dogovor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.nukuslab.dogovor.entity.User;
import uz.nukuslab.dogovor.security.Paydalaniwshi;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class DogovorApplication {

    @GetMapping
    public String home(@Paydalaniwshi User user){
        System.out.println(user.getFirstName());
//        return "Dogovor app home page";
        return user.getFirstName();
    }


    public static void main(String[] args) {
        SpringApplication.run(DogovorApplication.class, args);
    }

}
