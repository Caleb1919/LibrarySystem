package core;

import java.util.*;

public class Inventory {

    // Map: category → list of books in that category
    private Map<String, List<Book>> booksByCategory = new HashMap<>();

    // File for storing book data
    private static final String BOOKS_FILE = "data/books.txt";

    /**
     * Add a new book to inventory
     */
    public void addBook(Book book) {
        booksByCategory.putIfAbsent(book.getCategory(), new ArrayList<>());
        booksByCategory.get(book.getCategory()).add(book);
        System.out.println("✅ Book added successfully.");
    }

    /**
     * Remove a book by ISBN
     */
    public boolean removeBook(String isbn) {
        for (List<Book> bookList : booksByCategory.values()) {
            Iterator<Book> iterator = bookList.iterator();
            while (iterator.hasNext()) {
                Book b = iterator.next();
                if (b.getISBN().equalsIgnoreCase(isbn)) {
                    iterator.remove();
                    System.out.println("✅ Book removed successfully.");
                    return true;
                }
            }
        }
        System.out.println("⚠ Book not found.");
        return false;
    }

    public Map<String, List<Book>> getBooksByCategory() {
        return booksByCategory;
    }

    /**
     * List all books by category
     */
    public void listBooks() {
        if (booksByCategory.isEmpty()) {
            System.out.println("📂 No books in inventory.");
            return;
        }
        for (String category : booksByCategory.keySet()) {
            System.out.println("\n📚 Category: " + category);
            for (Book b : booksByCategory.get(category)) {
                System.out.println(b);
            }
        }
    }

    /**
     * Save books to file using FileManager
     */
    public void saveBooks() {
        List<String> lines = new ArrayList<>();
        for (List<Book> bookList : booksByCategory.values()) {
            for (Book b : bookList) {
                lines.add(b.getTitle() + "|" + b.getAuthor() + "|" + b.getISBN() + "|" +
                        b.getCategory() + "|" + b.getYear() + "|" + b.getPublisher() + "|" + b.getShelfLocation());
            }
        }
        FileManager.writeToFile(BOOKS_FILE, lines);
    }

    /**
     * Load books from file using FileManager
     */
    public void loadBooks() {
        List<String> lines = FileManager.readFromFile(BOOKS_FILE);
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length == 7) {
                Book b = new Book(data[0], data[1], data[2], data[3],
                        Integer.parseInt(data[4]), data[5], data[6]);
                addBook(b);
            }
        }
    }
}
