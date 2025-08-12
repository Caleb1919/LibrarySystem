package core;

import dsa.MyBST;
import dsa.MyStack;
import java.util.*;

public class BorrowerRegistry {

    // Map for quick ID lookup
    private Map<String, Borrower> borrowers = new HashMap<>();

    // BST for sorted order
    private MyBST<Borrower> borrowerTree = new MyBST<>();

    // Stack to store recently removed borrowers
    private MyStack<Borrower> removedBorrowers = new MyStack<>();

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
        borrowerTree.insert(borrower);
        System.out.println("✅ Borrower added successfully.");
    }

    /**
     * Get all borrowers sorted by ID
     */
    public List<Borrower> getAllBorrowers() {
        return borrowerTree.inOrderList();
    }

    /**
     * Remove a borrower by ID
     */
    public boolean removeBorrower(String id) {
        Borrower removed = borrowers.remove(id);
        if (removed != null) {
            borrowerTree.delete(removed);
            removedBorrowers.push(removed);
            System.out.println("✅ Borrower removed successfully.");
            return true;
        }
        System.out.println("⚠ Borrower not found.");
        return false;
    }

    /**
     * List all borrowers (sorted by ID)
     */
    public void listBorrowers() {
        List<Borrower> sortedList = borrowerTree.inOrderList();
        if (sortedList.isEmpty()) {
            System.out.println("📂 No borrowers registered.");
            return;
        }
        for (Borrower b : sortedList) {
            System.out.println(b);
        }
    }

    /**
     * Search borrower by ID using BST search
     */
    public Borrower findBorrowerById(String id) {
        for (Borrower b : borrowerTree.inOrderList()) {
            if (b.getId().equalsIgnoreCase(id)) return b;
        }
        return null;
    }

    /**
     * Save borrowers to file
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
     * Load borrowers from file
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
                borrowerTree.insert(b);
            }
        }
    }
}
