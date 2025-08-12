package core;

public class Book implements Comparable<Book> {
    private String title;
    private String author;
    private String ISBN;
    private String category;
    private int year;
    private String publisher;
    private String shelfLocation;

    public Book(String title, String author, String ISBN, String category, int year, String publisher, String shelfLocation) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.category = category;
        this.year = year;
        this.publisher = publisher;
        this.shelfLocation = shelfLocation;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public String getCategory() { return category; }
    public int getYear() { return year; }
    public String getPublisher() { return publisher; }
    public String getShelfLocation() { return shelfLocation; }

    @Override
    public String toString() {
        return String.format("📖 %s by %s | ISBN: %s | Year: %d | Publisher: %s | Shelf: %s",
                title, author, ISBN, year, publisher, shelfLocation);
    }

    // Needed for BST sorting (by ISBN here)
    @Override
    public int compareTo(Book other) {
        return this.ISBN.compareToIgnoreCase(other.ISBN);
    }
}
