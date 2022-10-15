package gruppe_b.quizduell.persistence;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class HelloWorldPersistence {
    public static void main(String args[]) {
        System.out.println("Hello, World! from persistence layer");
    }
}