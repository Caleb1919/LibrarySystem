package core;

import java.util.*;

public class BorrowerRegistry {

    // Map: borrower ID → Borrower object
    private Map<String, Borrower> borrowers = new HashMap<>();

    // File path for saving borrowers
    private static final String BORROWERS_FILE = "data/borrowers.txt";

    /**
     * Add a new borrower
     */
    public void addBorrower(Borrower borrower) {
        if (borrowers.containsKey(borrower.getId())) {
            System.out.println("⚠ Borrower with this ID already exists.");
            return;
        }
        borrowers.put(borrower.getId(), borrower);
        System.out.println("✅ Borrower added successfully.");
    }

    /**
     * Get all borrowers as a list
     */
    public List<Borrower> getAllBorrowers() {
        return new ArrayList<>(borrowers.values());
    }

    /**
     * Remove a borrower by ID
     */
    public boolean removeBorrower(String id) {
        if (borrowers.remove(id) != null) {
            System.out.println("✅ Borrower removed successfully.");
            return true;
        }
        System.out.println("⚠ Borrower not found.");
        return false;
    }

    /**
     * List all borrowers
     */
    public void listBorrowers() {
        if (borrowers.isEmpty()) {
            System.out.println("📂 No borrowers registered.");
            return;
        }
        for (Borrower b : borrowers.values()) {
            System.out.println(b);
        }
    }

    /**
     * Recursive search for borrower by ID
     */
    public Borrower findBorrowerById(String id) {
        return findBorrowerRecursive(new ArrayList<>(borrowers.values()), id, 0);
    }

    // Recursive helper method
    private Borrower findBorrowerRecursive(List<Borrower> list, String id, int index) {
        if (index >= list.size()) return null; // Base case: not found
        Borrower current = list.get(index);
        if (current.getId().equalsIgnoreCase(id)) return current; // Found
        return findBorrowerRecursive(list, id, index + 1); // Recursive call
    }

    /**
     * Save borrowers to file using FileManager
     */
    public void saveBorrowers() {
        List<String> lines = new ArrayList<>();
        for (Borrower b : borrowers.values()) {
            lines.add(b.getName() + "|" + b.getId() + "|" +
                      String.join(",", b.getBorrowedISBNs()) + "|" +
                      b.getFines() + "|" + b.getContactInfo());
        }
        FileManager.writeToFile(BORROWERS_FILE, lines);
    }

    /**
     * Load borrowers from file using FileManager
     */
    public void loadBorrowers() {
        List<String> lines = FileManager.readFromFile(BORROWERS_FILE);
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length == 5) {
                List<String> borrowedBooks = new ArrayList<>();
                if (!data[2].isEmpty()) {
                    borrowedBooks = Arrays.asList(data[2].split(","));
                }
                Borrower b = new Borrower(data[0], data[1], borrowedBooks,
                                          Double.parseDouble(data[3]), data[4]);
                borrowers.put(b.getId(), b);
            }
        }
    }
}
