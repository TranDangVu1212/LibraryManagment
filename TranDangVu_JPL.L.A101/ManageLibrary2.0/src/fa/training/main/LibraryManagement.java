package fa.training.main;

import fa.training.entities.Book;
import fa.training.entities.Magazine;
import fa.training.services.BookService;
import fa.training.services.MagazineService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class LibraryManagement {
    private static final Scanner scanner = new Scanner(System.in);

    // Instantiate our Services
    private static final BookService bookService = new BookService();
    private static final MagazineService magazineService = new MagazineService();

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1:
                    doAddNewBook();
                    break;
                case 2:
                    doAddNewMagazine();
                    break;
                case 3:
                    doDisplayByYearAndPublisher();
                    break;
                case 4:
                    doAddAuthorToBook();
                    break;
                case 5:
                    doDisplayTop10Magazines();
                    break;
                case 6:
                    doCountPublicationsByYear();
                    break;
                case 7:
                    doSearchPublications();
                    break;
                case 8:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    // ----------------------------------
    //         MENU & HELPER METHODS
    // ----------------------------------
    private static void printMenu() {
        System.out.println("\n====== Library Management ======");
        System.out.println("1. Add New Book");
        System.out.println("2. Add New Magazine");
        System.out.println("3. Display Books & Magazines (by Year & Publisher)");
        System.out.println("4. Add Author to an Existing Book");
        System.out.println("5. Display Top 10 Magazines by Volume");
        System.out.println("6. Count Publications by Year");
        System.out.println("7. Search Publications");
        System.out.println("8. Exit");
        System.out.println("================================");
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    private static String readNonEmptyString(String prompt) {
        System.out.print(prompt);
        String line;
        do {
            line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.print("Cannot be empty. " + prompt);
            }
        } while (line.isEmpty());
        return line;
    }

    private static LocalDate readDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine().trim();
            try {
                return LocalDate.parse(dateStr, formatter);  // dd-MM-yyyy
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Use dd-MM-yyyy.");
            }
        }
    }

    // ----------------------------------
    //         MENU OPTIONS
    // ----------------------------------

    // 1. Add New Book
    private static void doAddNewBook() {
        System.out.println("\n--- Add New Book ---");
        String isbn = readNonEmptyString("ISBN (unique): ");
        int year = readInt("Publication Year: ");
        String publisher = readNonEmptyString("Publisher: ");
        LocalDate pubDate = readDate("Publication Date (dd-MM-yyyy): ");

        // Authors input (comma-separated)
        System.out.print("Authors (comma-separated): ");
        String authorLine = scanner.nextLine().trim();
        Set<String> authors = new HashSet<>();
        if (!authorLine.isEmpty()) {
            String[] arr = authorLine.split(",");
            for (String a : arr) {
                authors.add(a.trim());
            }
        }

        String place = readNonEmptyString("Publication Place: ");

        // Create Book
        Book book = new Book(year, publisher, pubDate, isbn, authors, place);
        boolean added = bookService.addBook(book);
        if (added) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Error: ISBN already exists!");
        }
    }

    // 2. Add New Magazine
    private static void doAddNewMagazine() {
        System.out.println("\n--- Add New Magazine ---");
        int year = readInt("Publication Year: ");
        String publisher = readNonEmptyString("Publisher: ");
        LocalDate pubDate = readDate("Publication Date (dd-MM-yyyy): ");
        String author = readNonEmptyString("Author: ");
        int volume = readInt("Volume: ");
        int edition = readInt("Edition: ");

        Magazine mag = new Magazine(year, publisher, pubDate, author, volume, edition);
        magazineService.addMagazine(mag);
        System.out.println("Magazine added successfully!");
    }

    // 3. Display (Books & Magazines) by Year & Publisher
    private static void doDisplayByYearAndPublisher() {
        System.out.println("\n--- Display by Year & Publisher ---");
        int year = readInt("Publication Year: ");
        String publisher = readNonEmptyString("Publisher: ");

        // Books
        List<Book> matchingBooks = bookService.getBooksByYearAndPublisher(year, publisher);
        System.out.println("\n=== Matching Books ===");
        if (matchingBooks.isEmpty()) {
            System.out.println("No matching books found.");
        } else {
            for (Book b : matchingBooks) {
                b.display();
            }
        }

        // Magazines
        List<Magazine> matchingMags = magazineService.getMagazinesByYearAndPublisher(year, publisher);
        System.out.println("\n=== Matching Magazines ===");
        if (matchingMags.isEmpty()) {
            System.out.println("No matching magazines found.");
        } else {
            for (Magazine m : matchingMags) {
                m.display();
            }
        }
    }

    // 4. Add Author to Book
    private static void doAddAuthorToBook() {
        System.out.println("\n--- Add Author to Book ---");
        String isbn = readNonEmptyString("Enter book ISBN: ");
        String newAuthor = readNonEmptyString("Author name: ");

        String result = bookService.addAuthor(isbn, newAuthor);
        System.out.println(result);
    }

    // 5. Display Top 10 Magazines (by volume)
    private static void doDisplayTop10Magazines() {
        System.out.println("\n--- Top 10 Magazines by Volume ---");
        List<Magazine> top10 = magazineService.getTop10ByVolume();
        if (top10.isEmpty()) {
            System.out.println("No magazines found.");
        } else {
            for (Magazine m : top10) {
                m.display();
            }
        }
    }

    // 6. Count Publications by Year
    private static void doCountPublicationsByYear() {
        System.out.println("\n--- Count Publications by Year ---");
        int year = readInt("Publication Year: ");

        // Count matching books
        long bookCount = bookService.getAllBooks().stream()
                .filter(b -> b.getPublicationYear() == year)
                .count();

        // Count matching magazines
        long magCount = magazineService.getAllMagazines().stream()
                .filter(m -> m.getPublicationYear() == year)
                .count();

        long total = bookCount + magCount;
        System.out.println("Total publications in " + year + ": " + total);
    }

    // 7. Search Publications (ISBN, Author, Publisher)
    private static void doSearchPublications() {
        System.out.println("\n--- Search Publications ---");
        System.out.println("1. ISBN (Books only, exact match)");
        System.out.println("2. Author (Books or Magazines, partial match)");
        System.out.println("3. Publisher (Books or Magazines, partial match)");
        int option = readInt("Choose a search option: ");

        // Collect matches in a single list
        List<fa.training.entities.Publication> results = new ArrayList<>();

        switch (option) {
            case 1: // ISBN
                String isbn = readNonEmptyString("Enter ISBN: ");
                Book foundBook = bookService.findBookByIsbn(isbn);
                if (foundBook != null) {
                    results.add(foundBook);
                }
                break;

            case 2: // Author
                String authorPart = readNonEmptyString("Enter author (partial): ").toLowerCase();
                // Books
                for (Book b : bookService.getAllBooks()) {
                    for (String a : b.getAuthors()) {
                        if (a.toLowerCase().contains(authorPart)) {
                            results.add(b);
                            break;
                        }
                    }
                }
                // Magazines
                for (Magazine m : magazineService.getAllMagazines()) {
                    if (m.getAuthor().toLowerCase().contains(authorPart)) {
                        results.add(m);
                    }
                }
                break;

            case 3: // Publisher
                String pubPart = readNonEmptyString("Enter publisher (partial): ").toLowerCase();
                // Books
                for (Book b : bookService.getAllBooks()) {
                    if (b.getPublisher().toLowerCase().contains(pubPart)) {
                        results.add(b);
                    }
                }
                // Magazines
                for (Magazine m : magazineService.getAllMagazines()) {
                    if (m.getPublisher().toLowerCase().contains(pubPart)) {
                        results.add(m);
                    }
                }
                break;

            default:
                System.out.println("Invalid search option.");
                return;
        }

        // Sort results by publication date
        results.sort(Comparator.comparing(fa.training.entities.Publication::getPublicationDate));

        // Display
        if (results.isEmpty()) {
            System.out.println("No matching publications found.");
        } else {
            System.out.println("=== Search Results ===");
            for (fa.training.entities.Publication p : results) {
                p.display();
            }
        }
    }
}
