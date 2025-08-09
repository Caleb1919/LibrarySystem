package main;

import core.*;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {

        // Core modules
        Inventory inventory = new Inventory();
        BorrowerRegistry borrowerRegistry = new BorrowerRegistry();
        LendingTracker lendingTracker = new LendingTracker(inventory, borrowerRegistry);
        OverdueMonitor overdueMonitor = new OverdueMonitor(lendingTracker, borrowerRegistry);
        ReportGenerator reportGenerator = new ReportGenerator(lendingTracker, borrowerRegistry, inventory);

        // Load existing data
        inventory.loadBooks();
        borrowerRegistry.loadBorrowers();
        lendingTracker.loadTransactions();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== 📚 Library Management System =====");
            System.out.println("1. List All Books");
            System.out.println("2. Add Book");
            System.out.println("3. Remove Book");
            System.out.println("4. Register Borrower");
            System.out.println("5. Lend Book");
            System.out.println("6. Return Book");
            System.out.println("7. View Borrowers");
            System.out.println("8. View Transactions");
            System.out.println("9. Check Overdue Books");
            System.out.println("10. Reports Menu");
            System.out.println("11. Save & Exit");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    inventory.listBooks();
                    break;

                case "2":
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter publisher: ");
                    String publisher = scanner.nextLine();
                    System.out.print("Enter shelf location: ");
                    String shelf = scanner.nextLine();

                    inventory.addBook(new Book(title, author, isbn, category, year, publisher, shelf));
                    break;

                case "3":
                    System.out.print("Enter ISBN to remove: ");
                    String removeIsbn = scanner.nextLine();
                    inventory.removeBook(removeIsbn);
                    break;

                case "4":
                    System.out.print("Enter name: ");
                    String bName = scanner.nextLine();
                    System.out.print("Enter borrower ID: ");
                    String bId = scanner.nextLine();
                    System.out.print("Enter contact info: ");
                    String contact = scanner.nextLine();
                    borrowerRegistry.addBorrower(new Borrower(bName, bId, new java.util.ArrayList<>(), 0.0, contact));
                    break;

                case "5":
                    System.out.print("Enter ISBN: ");
                    String lendIsbn = scanner.nextLine();
                    System.out.print("Enter borrower ID: ");
                    String lendId = scanner.nextLine();
                    lendingTracker.lendBook(lendIsbn, lendId);
                    break;

                case "6":
                    System.out.print("Enter ISBN: ");
                    String returnIsbn = scanner.nextLine();
                    System.out.print("Enter borrower ID: ");
                    String returnId = scanner.nextLine();
                    lendingTracker.returnBook(returnIsbn, returnId);
                    break;

                case "7":
                    borrowerRegistry.listBorrowers();
                    break;

                case "8":
                    lendingTracker.listTransactions();
                    break;

                case "9":
                    overdueMonitor.loadBorrowedTransactions();
                    overdueMonitor.checkOverdues();
                    break;

                case "10":
                    boolean reportsRunning = true;
                    while (reportsRunning) {
                        System.out.println("\n===== 📊 Reports Menu =====");
                        System.out.println("1. Most Borrowed Books This Month");
                        System.out.println("2. Borrowers with Highest Fines");
                        System.out.println("3. Inventory Distribution by Category");
                        System.out.println("4. Back to Main Menu");
                        System.out.print("Enter choice: ");
                        String reportChoice = scanner.nextLine();

                        switch (reportChoice) {
                            case "1":
                                reportGenerator.mostBorrowedBooksThisMonth();
                                break;
                            case "2":
                                reportGenerator.borrowersWithHighestFines();
                                break;
                            case "3":
                                reportGenerator.inventoryDistribution();
                                break;
                            case "4":
                                reportsRunning = false;
                                break;
                            default:
                                System.out.println("⚠ Invalid option.");
                        }
                    }
                    break;

                case "11":
                    inventory.saveBooks();
                    borrowerRegistry.saveBorrowers();
                    lendingTracker.saveTransactions();
                    System.out.println("💾 Data saved. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("⚠ Invalid choice, try again.");
            }
        }

        scanner.close();
    }
} // then update main with respect to that 