package Utils;

import core.Book;

import java.util.ArrayList;
import java.util.List;

public class SortUtils {

    /**
     * Selection sort by title
     */
    public static void selectionSortByTitle(List<Book> books) {
        int n = books.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (books.get(j).getTitle().compareToIgnoreCase(books.get(minIndex).getTitle()) < 0) {
                    minIndex = j;
                }
            }
            Book temp = books.get(minIndex);
            books.set(minIndex, books.get(i));
            books.set(i, temp);
        }
    }

    /**
     * Merge sort by publication year
     */
    public static List<Book> mergeSortByYear(List<Book> books) {
        if (books.size() <= 1) {
            return books;
        }
        int mid = books.size() / 2;
        List<Book> left = mergeSortByYear(new ArrayList<>(books.subList(0, mid)));
        List<Book> right = mergeSortByYear(new ArrayList<>(books.subList(mid, books.size())));
        return merge(left, right);
    }

    private static List<Book> merge(List<Book> left, List<Book> right) {
        List<Book> merged = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getYear() <= right.get(j).getYear()) {
                merged.add(left.get(i++));
            } else {
                merged.add(right.get(j++));
            }
        }

        while (i < left.size()) merged.add(left.get(i++));
        while (j < right.size()) merged.add(right.get(j++));
        return merged;
    }
}
