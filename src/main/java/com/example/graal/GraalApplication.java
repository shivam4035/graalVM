package com.example.graal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Collection;
import java.util.stream.Stream;

@SpringBootApplication
public class GraalApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraalApplication.class, args);
	}

}

@RestController
@RequiredArgsConstructor
class CustomerRestController {

	private final CustomerRepository customerRepository;

	@GetMapping("/customers")
	Collection<Customer> customers() {
		return this.customerRepository.findAll();
	}
}

@Component
@RequiredArgsConstructor
class Initializer implements ApplicationListener<ApplicationReadyEvent> {

	private final CustomerRepository customerRepository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		Stream.of("xyz", "Dr. A", "abc")
				.map(name -> new Customer(null, name))
				.map(this.customerRepository::save)
				.forEach(System.out::println);
	}
}

interface CustomerRepository extends JpaRepository<Customer, Integer> {

}

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {

	@Id
	@GeneratedValue
	private Integer id;
	private String name;

}
