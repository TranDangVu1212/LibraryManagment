package fa.training.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Book class extending Publication.
 */
public class Book extends Publication {
    private String isbn;                // Unique
    private Set<String> authors;        // A set of authors
    private String publicationPlace;

    public Book(int publicationYear, String publisher, LocalDate publicationDate,
                String isbn, Set<String> authors, String publicationPlace) {
        super(publicationYear, publisher, publicationDate);
        this.isbn = isbn;
        this.authors = (authors != null) ? authors : new HashSet<>();
        this.publicationPlace = publicationPlace;
    }

    public String getIsbn() {
        return isbn;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public boolean addAuthor(String author) {
        return this.authors.add(author);
    }

    public String getPublicationPlace() {
        return publicationPlace;
    }

    @Override
    public void display() {
        System.out.println("=== Book Info ===");
        System.out.println("ISBN: " + isbn);
        System.out.println("Year: " + publicationYear);
        System.out.println("Publisher: " + publisher);
        System.out.println("Publication Date: " + publicationDate);
        System.out.println("Authors: " + authors);
        System.out.println("Publication Place: " + publicationPlace);
        System.out.println("=================");
    }
}
