package core;

import java.util.*;

public class BookSorter {

    private Inventory inventory;

    public BookSorter(Inventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Selection Sort books by title (A–Z)
     */
    public List<Book> selectionSortByTitle() {
        List<Book> allBooks = getAllBooks();
        int n = allBooks.size();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (allBooks.get(j).getTitle().compareToIgnoreCase(allBooks.get(minIndex).getTitle()) < 0) {
                    minIndex = j;
                }
            }
            Collections.swap(allBooks, i, minIndex);
        }
        return allBooks;
    }

    /**
     * Merge Sort books by publication year (oldest → newest)
     */
    public List<Book> mergeSortByYear() {
        List<Book> allBooks = getAllBooks();
        return mergeSort(allBooks);
    }

    private List<Book> mergeSort(List<Book> books) {
        if (books.size() <= 1) {
            return books;
        }

        int mid = books.size() / 2;
        List<Book> left = mergeSort(new ArrayList<>(books.subList(0, mid)));
        List<Book> right = mergeSort(new ArrayList<>(books.subList(mid, books.size())));

        return merge(left, right);
    }

    private List<Book> merge(List<Book> left, List<Book> right) {
        List<Book> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).getYear() <= right.get(j).getYear()) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));

        return result;
    }

    /**
     * Get books sorted by ISBN using the MyBST from Inventory (fast)
     */
    public List<Book> getBooksSortedByISBN() {
        return inventory.getBookTree().inOrderList(); // direct BST traversal
    }

    /**
     * Helper to get all books from inventory
     */
    private List<Book> getAllBooks() {
        List<Book> allBooks = new ArrayList<>();
        for (List<Book> list : inventory.getBooksByCategory().values()) {
            allBooks.addAll(list);
        }
        return allBooks;
    }
}
