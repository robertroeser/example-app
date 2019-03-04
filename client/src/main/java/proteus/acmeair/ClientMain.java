package proteus.acmeair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "proteus.acmeair",
    excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "proteus\\.acmeair\\.contract\\..*"))
public class ClientMain {

  public static void main(String... args) {
    SpringApplication.run(ClientMain.class, args);
  }
}
