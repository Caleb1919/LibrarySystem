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
     * Binary search for book by ISBN (case-insensitive)
     * Requires sorting the list of all books first
     */
    public Book binarySearchByISBN(String isbn) {
        List<Book> allBooks = new ArrayList<>();
        for (List<Book> list : inventory.getBooksByCategory().values()) {
            allBooks.addAll(list);
        }

        // Sort by ISBN before binary search
        allBooks.sort(Comparator.comparing(Book::getISBN, String.CASE_INSENSITIVE_ORDER));

        int left = 0;
        int right = allBooks.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = allBooks.get(mid);
            int cmp = midBook.getISBN().compareToIgnoreCase(isbn);

            if (cmp == 0) {
                return midBook; // Found
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null; // Not found
    }
}
