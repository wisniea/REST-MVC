package pl.wisniea.restmvc.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.wisniea.restmvc.domain.Book;
import pl.wisniea.restmvc.repositories.BookRepository;

@Component
public class BootStrapDataBooks implements CommandLineRunner {
    private  final BookRepository bookRepository;
    public BootStrapDataBooks(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        System.out.println("Loading Book Data");

        Book b1 = new Book();
        b1.setAuthor("Joanne K. Rowling");
        b1.setTitle("Harry Potter and the Prisoner of Azkaban");
        b1.setGenre("fantasy");
        b1.setAvailability(true);
        bookRepository.save(b1);

        Book b2 = new Book();
        b2.setAuthor("Gillian Flynn");
        b2.setTitle("Gone Girl");
        b2.setGenre("thriller");
        b2.setAvailability(false);
        bookRepository.save(b2);

        Book b3 = new Book();
        b3.setAuthor("Colleen Hoover");
        b3.setTitle("It Ends with Us: A Novel");
        b3.setGenre("romance");
        b3.setAvailability(true);
        bookRepository.save(b3);

        System.out.println("Books Saved: " + bookRepository.count());
    }
}
