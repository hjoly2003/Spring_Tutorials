package com.rest.payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Populates the database with {@code Employee} records.<p>
 * When Spring loads the class 
 * <ul>
 *  <li>Spring Boot will run ALL {@code CommandLineRunner} beans once the application context is loaded.</li>
 *  <li>This runner will request a copy of the {@code EmployeeRepository} you just created.</li>
 *  <li>Using it, it will create two entities and store them.</li>
 * </ul>
 */
@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository, OrderRepository orderRepository) {
        return args -> {
            log.info("Preloading " + repository.save(new Employee("Bilbo", "Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo", "Baggins", "thief")));

            orderRepository.save(new Order("MacBook Prob", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
