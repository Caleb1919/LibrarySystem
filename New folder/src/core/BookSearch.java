package core;

import java.util.*;

public class BookSearch {

    private Inventory inventory;

    public BookSearch(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Linear search for books by title (case-insensitive)
     */
    public List<Book> searchByTitle(String title) {
        List<Book> results = new ArrayList<>();
        for (List<Book> bookList : inventory.getBooksByCategory().values()) {
            for (Book b : bookList) {
                if (b.getTitle().toLowerCase().contains(title.toLowerCase())) {
                    results.add(b);
                }
            }
        }
        return results;
    }

    /**
     * Linear search for books by author (case-insensitive)
     */
    public List<Book> searchByAuthor(String author) {
        List<Book> results = new ArrayList<>();
        for (List<Book> bookList : inventory.getBooksByCategory().values()) {
            for (Book b : bookList) {
                if (b.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                    results.add(b);
                }
            }
        }
        return results;
    }

    /**
     * BST search for book by ISBN (fast)
     */
    public Book searchByISBN(String isbn) {
        return inventory.searchBook(isbn); // Uses MyBST search from Inventory
    }
}
