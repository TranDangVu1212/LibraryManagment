package fa.training.services;

import fa.training.entities.Book;
import java.util.*;
import java.util.stream.Collectors;

public class BookService {
    // In-memory list of Books
    private final List<Book> books = new ArrayList<>();

    public boolean addBook(Book book) {
        // Check unique ISBN
        if (findBookByIsbn(book.getIsbn()) != null) {
            return false; // ISBN already exists
        }
        books.add(book);
        return true;
    }

    public Book findBookByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    public String addAuthor(String isbn, String authorName) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            return "Book not found!";
        }
        boolean added = book.addAuthor(authorName);
        return (added) ? "Add successfully" : "Author existed";
    }

    public List<Book> getBooksByYearAndPublisher(int year, String publisher) {
        return books.stream()
                .filter(b -> b.getPublicationYear() == year
                        && b.getPublisher().equalsIgnoreCase(publisher))
                .collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return books;
    }
}
