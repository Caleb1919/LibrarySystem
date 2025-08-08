package Utils;

import core.Book;
import core.Borrower;

import java.util.List;

public class SearchUtils {

    /**
     * Linear search for a book by title
     */
    public static Book linearSearchBookByTitle(List<Book> books, String title) {
        for (Book b : books) {
            if (b.getTitle().equalsIgnoreCase(title)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Linear search for a borrower by name
     */
    public static Borrower linearSearchBorrowerByName(List<Borrower> borrowers, String name) {
        for (Borrower b : borrowers) {
            if (b.getName().equalsIgnoreCase(name)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Binary search for a book by title (list must be sorted by title)
     */
    public static Book binarySearchBookByTitle(List<Book> books, String title) {
        int low = 0, high = books.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            int comparison = books.get(mid).getTitle().compareToIgnoreCase(title);
            if (comparison == 0) {
                return books.get(mid);
            } else if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }
}
