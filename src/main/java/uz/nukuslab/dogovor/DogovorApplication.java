package uz.nukuslab.dogovor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class DogovorApplication {

    @GetMapping
    public String home(){
        return "Dogovor app home page";
    }


    public static void main(String[] args) {
        SpringApplication.run(DogovorApplication.class, args);
    }

}
