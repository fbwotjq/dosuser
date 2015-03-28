package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.system.ApplicationPidFileWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration 
public class ActuatorDemoApplication {

    public static void main(String[] args) {
    	SpringApplication springApplication = new SpringApplication(ActuatorDemoApplication.class);
    	springApplication.addListeners(new ApplicationPidFileWriter());
    	springApplication.run(args);
    }
}
