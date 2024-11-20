package bsg.application.anagrams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(
//        basePackages = {
//                "bsg.application.anagrams"
//        }
//)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
