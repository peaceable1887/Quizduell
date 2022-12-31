package gruppe_b.quizduell.infrastructure;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class HelloWorldInfrastructure {
    public static void main(String args[]) {
        System.out.println("Hello, World! from persistence layer");
    }
}