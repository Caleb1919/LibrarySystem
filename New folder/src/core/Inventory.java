package core;

import java.util.*;

public class Inventory {

    // Map: category → list of books in that category
    private Map<String, List<Book>> booksByCategory = new HashMap<>();

    // Root of the Binary Search Tree (BST) for all books
    private BookNode root;

    // File for storing book data
    private static final String BOOKS_FILE = "data/books.txt";

    /**
     * Node class for Binary Search Tree
     */
    private static class BookNode {
        Book book;
        BookNode left, right;

        BookNode(Book book) {
            this.book = book;
        }
    }

    /**
     * Add a new book to inventory
     */
    public void addBook(Book book) {
        booksByCategory.putIfAbsent(book.getCategory(), new ArrayList<>());
        booksByCategory.get(book.getCategory()).add(book);

        // Insert into BST for sorted storage
        root = insertIntoBST(root, book);

        System.out.println("✅ Book added successfully.");
    }

    /**
     * BST insertion (sorted by ISBN)
     */
    private BookNode insertIntoBST(BookNode node, Book book) {
        if (node == null) return new BookNode(book);

        if (book.getISBN().compareToIgnoreCase(node.book.getISBN()) < 0) {
            node.left = insertIntoBST(node.left, book);
        } else if (book.getISBN().compareToIgnoreCase(node.book.getISBN()) > 0) {
            node.right = insertIntoBST(node.right, book);
        }
        return node;
    }

    /**
     * Search for a book by ISBN using BST
     */
    public Book searchBook(String isbn) {
        return searchBST(root, isbn);
    }

    private Book searchBST(BookNode node, String isbn) {
        if (node == null) return null;

        if (isbn.equalsIgnoreCase(node.book.getISBN())) return node.book;
        if (isbn.compareToIgnoreCase(node.book.getISBN()) < 0) {
            return searchBST(node.left, isbn);
        } else {
            return searchBST(node.right, isbn);
        }
    }

    /**
     * Remove a book by ISBN (removes from map and BST)
     */
    public boolean removeBook(String isbn) {
        boolean removedFromMap = false;

        for (List<Book> bookList : booksByCategory.values()) {
            Iterator<Book> iterator = bookList.iterator();
            while (iterator.hasNext()) {
                Book b = iterator.next();
                if (b.getISBN().equalsIgnoreCase(isbn)) {
                    iterator.remove();
                    removedFromMap = true;
                    break;
                }
            }
        }

        if (removedFromMap) {
            root = removeFromBST(root, isbn);
            System.out.println("✅ Book removed successfully.");
            return true;
        }

        System.out.println("⚠ Book not found.");
        return false;
    }

    /**
     * BST removal
     */
    private BookNode removeFromBST(BookNode node, String isbn) {
        if (node == null) return null;

        if (isbn.compareToIgnoreCase(node.book.getISBN()) < 0) {
            node.left = removeFromBST(node.left, isbn);
        } else if (isbn.compareToIgnoreCase(node.book.getISBN()) > 0) {
            node.right = removeFromBST(node.right, isbn);
        } else {
            // Node to delete found
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;

            // Node with two children: get inorder successor
            node.book = findMin(node.right);
            node.right = removeFromBST(node.right, node.book.getISBN());
        }
        return node;
    }

    private Book findMin(BookNode node) {
        while (node.left != null) node = node.left;
        return node.book;
    }

    /**
     * Get sorted list of books using in-order BST traversal
     */
    public void listBooksSorted() {
        if (root == null) {
            System.out.println("📂 No books in inventory.");
            return;
        }
        System.out.println("📚 Books (sorted by ISBN):");
        inOrderTraversal(root);
    }

    private void inOrderTraversal(BookNode node) {
        if (node != null) {
            inOrderTraversal(node.left);
            System.out.println(node.book);
            inOrderTraversal(node.right);
        }
    }

    public Map<String, List<Book>> getBooksByCategory() {
        return booksByCategory;
    }

    /**
     * Normal category-wise listing
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
                addBook(b); // this adds to map and BST
            }
        }
    }
}
