package core;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class OverdueMonitor {

    private PriorityQueue<Transaction> overdueQueue;

    private LendingTracker lendingTracker;
    private BorrowerRegistry borrowerRegistry;

    private static final double DAILY_FINE = 2.0; // GHS 2 per day overdue

    public OverdueMonitor(LendingTracker lendingTracker, BorrowerRegistry borrowerRegistry) {
        this.lendingTracker = lendingTracker;
        this.borrowerRegistry = borrowerRegistry;

        // Sort by soonest due date (smallest borrowDate)
        overdueQueue = new PriorityQueue<>(Comparator.comparing(Transaction::getBorrowDate));
    }

    /**
     * Load all BORROWED transactions into priority queue
     */
    public void loadBorrowedTransactions() {
        overdueQueue.clear();
        for (Transaction t : lendingTracker.getAllTransactions()) {
            if (t.getStatus().equals("BORROWED")) {
                overdueQueue.add(t);
            }
        }
    }

    /**
     * Check and update overdue fines
     */
    public void checkOverdues() {
        if (overdueQueue.isEmpty()) {
            System.out.println("📂 No borrowed books to check.");
            return;
        }

        LocalDate today = LocalDate.now();
        boolean anyOverdue = false;

        while (!overdueQueue.isEmpty()) {
            Transaction t = overdueQueue.poll();
            long daysBorrowed = ChronoUnit.DAYS.between(t.getBorrowDate(), today);

            if (daysBorrowed > 14) {
                anyOverdue = true;
                long overdueDays = daysBorrowed - 14;
                double fineAmount = overdueDays * DAILY_FINE;

                Borrower borrower = borrowerRegistry.findBorrowerById(t.getBorrowerId());
                if (borrower != null) {
                    borrower.updateFines(fineAmount);
                    System.out.printf("⚠ Overdue: ISBN %s | Borrower %s | %d days overdue | Fine GHS %.2f%n",
                            t.getBookISBN(), borrower.getName(), overdueDays, fineAmount);
                }
            }
        }

        if (!anyOverdue) {
            System.out.println("✅ No overdue books found.");
        }
    }
}
