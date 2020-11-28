package pl.wisniea.restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wisniea.restmvc.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
