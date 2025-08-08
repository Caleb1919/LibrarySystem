package core;

import java.time.LocalDate;

public class Transaction {
    private String bookISBN;
    private String borrowerId;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status; // BORROWED or RETURNED

    public Transaction(String bookISBN, String borrowerId, LocalDate borrowDate, LocalDate returnDate, String status) {
        this.bookISBN = bookISBN;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String getBookISBN() { return bookISBN; }
    public String getBorrowerId() { return borrowerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public String getStatus() { return status; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("📄 ISBN: %s | Borrower: %s | Borrowed: %s | Returned: %s | Status: %s",
                bookISBN, borrowerId, borrowDate,
                (returnDate != null ? returnDate : "N/A"),
                status);
    }
}
