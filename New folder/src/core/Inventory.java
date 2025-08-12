package core;

import dsa.MyBST;
import dsa.MyQueue;
import dsa.MyStack;
import java.util.*;

/**
 * Inventory manages books in two complementary structures:
 * - booksByCategory (Map for grouping / listing by category)
 * - bookTree (BST for fast ordered operations / sorted traversal)
 *
 * Both exist because each structure solves a different use case:
 * - Map gives quick grouping and iteration by category
 * - BST gives ordered traversal and reasonably fast search/insertion
 */

public class Inventory {

    // Map: category → list of books in that category
    private Map<String, List<Book>> booksByCategory = new HashMap<>();

    // Binary Search Tree for all books (sorted by ISBN)
    private MyBST<Book> bookTree = new MyBST<>();

    // Stack to store recently removed books (for undo feature)
    private MyStack<Book> removedBooks = new MyStack<>();

    // Queue to store recently added books
    private MyQueue<Book> recentBooks = new MyQueue<>();

    // File for storing book data
    private static final String BOOKS_FILE = "data/books.txt";

    /** Add a new book to inventory */
    public void addBook(Book book) {
        booksByCategory.putIfAbsent(book.getCategory(), new ArrayList<>());
        booksByCategory.get(book.getCategory()).add(book);

        // Add to BST
        bookTree.insert(book);

        // Track in queue
        recentBooks.enqueue(book);

        System.out.println("✅ Book added successfully.");
    }

    /** Search for a book by ISBN using BST */
    public Book searchBook(String isbn) {
        for (Book b : bookTree.inOrderList()) {
            if (b.getISBN().equalsIgnoreCase(isbn)) return b;
        }
        return null;
    }

    /** Remove a book by ISBN */
    public boolean removeBook(String isbn) {
        Book bookToRemove = searchBook(isbn);
        if (bookToRemove != null) {
            booksByCategory.get(bookToRemove.getCategory())
                    .removeIf(b -> b.getISBN().equalsIgnoreCase(isbn));

            bookTree.delete(bookToRemove);
            removedBooks.push(bookToRemove);

            System.out.println("✅ Book removed successfully.");
            return true;
        }
        System.out.println("⚠ Book not found.");
        return false;
    }

    /** Get sorted list of books (in-order traversal) */
    public void listBooksSorted() {
        List<Book> sortedBooks = bookTree.inOrderList();
        if (sortedBooks.isEmpty()) {
            System.out.println("📂 No books in inventory.");
            return;
        }
        System.out.println("📚 Books (sorted by ISBN):");
        for (Book b : sortedBooks) {
            System.out.println(b);
        }
    }

    /** Normal category-wise listing */
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

    /** Save books to file */
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

    /** Load books from file */
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

    // ===== GETTERS =====
    public Map<String, List<Book>> getBooksByCategory() {
        return booksByCategory;
    }

    public MyBST<Book> getBookTree() {
        return bookTree;
    }

    public MyStack<Book> getRemovedBooksStack() {
        return removedBooks;
    }

    public MyQueue<Book> getRecentBooksQueue() {
        return recentBooks;
    }
}
