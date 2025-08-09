package core;

import java.time.LocalDate;
import java.util.*;


public class ReportGenerator {

    private LendingTracker lendingTracker;
    private BorrowerRegistry borrowerRegistry;
    private Inventory inventory;

    public ReportGenerator(LendingTracker lendingTracker, BorrowerRegistry borrowerRegistry, Inventory inventory) {
        this.lendingTracker = lendingTracker;
        this.borrowerRegistry = borrowerRegistry;
        this.inventory = inventory;
    }

    /**
     * Most borrowed books in the current month
     */
    public void mostBorrowedBooksThisMonth() {
        Map<String, Integer> borrowCount = new HashMap<>();
        LocalDate now = LocalDate.now();

        for (Transaction t : lendingTracker.getAllTransactions()) {
            if ("BORROWED".equalsIgnoreCase(t.getStatus()) || "RETURNED".equalsIgnoreCase(t.getStatus())) {
                if (t.getBorrowDate().getMonth() == now.getMonth() && t.getBorrowDate().getYear() == now.getYear()) {
                    borrowCount.put(t.getBookISBN(), borrowCount.getOrDefault(t.getBookISBN(), 0) + 1);
                }
            }
        }

        if (borrowCount.isEmpty()) {
            System.out.println("📂 No borrowings this month.");
            return;
        }

        System.out.println("📊 Most Borrowed Books This Month:");
        borrowCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(e -> {
                    Book b = findBookByISBN(e.getKey());
                    System.out.println(b.getTitle() + " - " + e.getValue() + " times");
                });
    }

    /**
     * Borrowers with highest outstanding fines
     */
    public void borrowersWithHighestFines() {
        List<Borrower> borrowers = new ArrayList<>(borrowerRegistry.getAllBorrowers());

        if (borrowers.isEmpty()) {
            System.out.println("📂 No borrowers found.");
            return;
        }

        System.out.println("💰 Borrowers with Highest Fines:");
        borrowers.stream()
                .sorted((b1, b2) -> Double.compare(b2.getFines(), b1.getFines()))
                .limit(5)
                .forEach(b -> System.out.printf("%s (ID: %s) - Fine: GHS %.2f%n", b.getName(), b.getId(), b.getFines()));
    }

    /**
     * Inventory distribution across categories
     */
    public void inventoryDistribution() {
        Map<String, List<Book>> booksByCategory = inventory.getBooksByCategory();
        if (booksByCategory.isEmpty()) {
            System.out.println("📂 No books in inventory.");
            return;
        }

        System.out.println("📚 Inventory Distribution by Category:");
        for (String category : booksByCategory.keySet()) {
            System.out.printf("%s: %d books%n", category, booksByCategory.get(category).size());
        }
    }

    /**
     * Helper to find a book by ISBN
     */
    private Book findBookByISBN(String isbn) {
        for (List<Book> books : inventory.getBooksByCategory().values()) {
            for (Book b : books) {
                if (b.getISBN().equalsIgnoreCase(isbn)) {
                    return b;
                }
            }
        }
        return null;
    }
}
