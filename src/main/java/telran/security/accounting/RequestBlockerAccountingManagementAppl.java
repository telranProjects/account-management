package telran.security.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "telran")
public class RequestBlockerAccountingManagementAppl {

	public static void main(String[] args) {
		SpringApplication.run(RequestBlockerAccountingManagementAppl.class, args);
	}

}
