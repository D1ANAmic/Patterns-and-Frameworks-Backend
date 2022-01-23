package frisbee.puf.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BackendApplication {

    /**
     * Starts the backend application.
     *
     * @param args optional arguments that can be passed
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
