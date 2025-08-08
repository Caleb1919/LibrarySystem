package core;

import java.time.LocalDate;
import java.util.*;

public class LendingTracker {

    private Queue<Transaction> transactions = new LinkedList<>();
    private static final String TRANSACTIONS_FILE = "data/transactions.txt";

    private Inventory inventory;
    private BorrowerRegistry borrowerRegistry;

    public LendingTracker(Inventory inventory, BorrowerRegistry borrowerRegistry) {
        this.inventory = inventory;
        this.borrowerRegistry = borrowerRegistry;
    }

    /**
     * Lend a book to a borrower
     */
    public void lendBook(String isbn, String borrowerId) {
        Borrower borrower = borrowerRegistry.findBorrowerById(borrowerId);
        if (borrower == null) {
            System.out.println("⚠ Borrower not found.");
            return;
        }

        Book bookToLend = inventorySearch(isbn);
        if (bookToLend == null) {
            System.out.println("⚠ Book not found in inventory.");
            return;
        }

        // Check if already borrowed by this borrower and not returned
        for (Transaction t : transactions) {
            if (t.getBookISBN().equalsIgnoreCase(isbn) &&
                t.getBorrowerId().equalsIgnoreCase(borrowerId) &&
                t.getStatus().equals("BORROWED")) {
                System.out.println("⚠ This borrower already has this book and has not returned it yet.");
                return;
            }
        }

        borrower.borrowBook(isbn);
        Transaction transaction = new Transaction(isbn, borrowerId, LocalDate.now(), null, "BORROWED");
        transactions.add(transaction);

        System.out.println("✅ Book lent successfully to " + borrower.getName());
    }

    /**
     * Return a book from a borrower
     */
    public void returnBook(String isbn, String borrowerId) {
        Borrower borrower = borrowerRegistry.findBorrowerById(borrowerId);
        if (borrower == null) {
            System.out.println("⚠ Borrower not found.");
            return;
        }

        boolean found = false;
        for (Transaction t : transactions) {
            if (t.getBookISBN().equalsIgnoreCase(isbn) &&
                t.getBorrowerId().equalsIgnoreCase(borrowerId) &&
                t.getStatus().equals("BORROWED")) {

                borrower.returnBook(isbn);
                t.setReturnDate(LocalDate.now());
                t.setStatus("RETURNED");
                System.out.println("✅ Book returned successfully by " + borrower.getName());
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("⚠ No active lending record found for this book and borrower.");
        }
    }

    /**
     * List all current transactions
     */
    public void listTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("📂 No lending transactions recorded.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    /**
     * Save transactions to file using FileManager
     */
    public void saveTransactions() {
        List<String> lines = new ArrayList<>();
        for (Transaction t : transactions) {
            lines.add(t.getBookISBN() + "|" + t.getBorrowerId() + "|" +
                      t.getBorrowDate() + "|" +
                      (t.getReturnDate() != null ? t.getReturnDate() : "") + "|" +
                      t.getStatus());
        }
        FileManager.writeToFile(TRANSACTIONS_FILE, lines);
    }

    /**
     * Load transactions from file using FileManager
     */
    public void loadTransactions() {
        List<String> lines = FileManager.readFromFile(TRANSACTIONS_FILE);
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length == 5) {
                LocalDate borrowDate = LocalDate.parse(data[2]);
                LocalDate returnDate = data[3].isEmpty() ? null : LocalDate.parse(data[3]);
                Transaction t = new Transaction(data[0], data[1], borrowDate, returnDate, data[4]);
                transactions.add(t);
            }
        }
    }

    /**
     * Get all transactions (used by OverdueMonitor, ReportGenerator)
     */
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Helper method to find a book in inventory by ISBN
     */
    private Book inventorySearch(String isbn) {
        try {
            for (List<Book> bookList : inventory.getBooksByCategory().values()) {
                for (Book b : bookList) {
                    if (b.getISBN().equalsIgnoreCase(isbn)) {
                        return b;
                    }
                }
            }
        } catch (Exception e) {
            // Ignore if inventory is empty
        }
        return null;
    }
}
