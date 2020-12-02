package pl.wisniea.restmvc.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.wisniea.restmvc.domain.Customer;
import pl.wisniea.restmvc.repositories.CustomerRepository;

@Component
public class BootStrapData implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    public BootStrapData(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Loading Customer Data");
        Customer c1 = new Customer();
        c1.setFirstName("Michael");
        c1.setLastName("Jordan");
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setFirstName("George");
        c2.setLastName("Weston");
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setFirstName("Julia");
        c3.setLastName("Roberts");
        customerRepository.save(c3);

        System.out.println("Customers Saved: " + customerRepository.count());
    }
}
