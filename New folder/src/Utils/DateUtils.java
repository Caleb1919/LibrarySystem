package Utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    private static final int ALLOWED_DAYS = 14; // Loan period

    /**
     * Calculate due date given a borrow date
     */
    public static LocalDate calculateDueDate(LocalDate borrowDate) {
        return borrowDate.plusDays(ALLOWED_DAYS);
    }

    /**
     * Check if a book is overdue
     */
    public static boolean isOverdue(LocalDate borrowDate) {
        return LocalDate.now().isAfter(calculateDueDate(borrowDate));
    }

    /**
     * Get days overdue
     */
    public static long getDaysOverdue(LocalDate borrowDate) {
        if (!isOverdue(borrowDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(calculateDueDate(borrowDate), LocalDate.now());
    }
}
