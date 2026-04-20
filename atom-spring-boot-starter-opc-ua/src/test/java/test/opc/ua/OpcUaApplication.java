package test.opc.ua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Catch
 * @since 2024-08-07
 */
@SpringBootApplication
public class OpcUaApplication {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(OpcUaApplication.class, args);
		Thread.sleep(100000L);
	}

}
