package core;

import java.util.*;

public class Borrower {
    private String name;
    private String id;
    private List<String> borrowedISBNs;
    private double fines;
    private String contactInfo;

    public Borrower(String name, String id, List<String> borrowedISBNs, double fines, String contactInfo) {
        this.name = name;
        this.id = id;
        this.borrowedISBNs = new ArrayList<>(borrowedISBNs);
        this.fines = fines;
        this.contactInfo = contactInfo;
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public List<String> getBorrowedISBNs() { return borrowedISBNs; }
    public double getFines() { return fines; }
    public String getContactInfo() { return contactInfo; }

    public void borrowBook(String isbn) {
        borrowedISBNs.add(isbn);
    }

    public void returnBook(String isbn) {
        borrowedISBNs.remove(isbn);
    }

    public void updateFines(double amount) {
        fines += amount;
    }

    @Override
    public String toString() {
        return String.format("👤 %s | ID: %s | Borrowed Books: %s | Fines: %.2f | Contact: %s",
                name, id, borrowedISBNs, fines, contactInfo);
    }
}
