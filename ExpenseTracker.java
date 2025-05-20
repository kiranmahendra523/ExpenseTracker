import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ExpenseTracker {

    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Expense Tracker ===");
            System.out.println("1. Add Income/Expense");
            System.out.println("2. Load Transactions from File");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> addTransaction();
                case 2 -> loadFile();
                case 3 -> viewMonthlySummary();
                case 4 -> {
                    System.out.println("Exiting. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void addTransaction() {
        System.out.print("Enter transaction type (income/expense): ");
        String type = scanner.nextLine().trim().toLowerCase();

        String subcategory = "";
        if ("income".equals(type)) {
            System.out.print("Enter subcategory (salary/business): ");
        } else if ("expense".equals(type)) {
            System.out.print("Enter subcategory (food/rent/travel): ");
        } else {
            System.out.println("Invalid type.");
            return;
        }
        subcategory = scanner.nextLine().trim().toLowerCase();

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine(), dateFormatter);

        transactions.add(new Transaction(type, subcategory, amount, date));
        System.out.println("Transaction added successfully.");
    }

    private static void loadFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the CSV file (e.g., C:\\Users\\user\\transactions.csv): ");
        String filePath = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int loaded = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String type = parts[0].trim().toLowerCase();
                String subcategory = parts[1].trim().toLowerCase();
                double amount = Double.parseDouble(parts[2].trim());
                LocalDate date = LocalDate.parse(parts[3].trim(), dateFormatter);

                transactions.add(new Transaction(type, subcategory, amount, date));
                loaded++;
            }
            System.out.println("Loaded " + loaded + " transactions from file.");
        } catch (IOException | DateTimeParseException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }


    private static void viewMonthlySummary() {
        Map<YearMonth, List<Transaction>> monthlyMap = new TreeMap<>();

        for (Transaction t : transactions) {
            YearMonth ym = YearMonth.from(t.date);
            monthlyMap.computeIfAbsent(ym, k -> new ArrayList<>()).add(t);
        }

        if (monthlyMap.isEmpty()) {
            System.out.println("No transactions to summarize.");
            return;
        }

        System.out.println("\n--- Full Monthly Summary ---");

        for (YearMonth ym : monthlyMap.keySet()) {
            double income = 0, expense = 0;
            Map<String, Double> categoryTotals = new HashMap<>();

            for (Transaction t : monthlyMap.get(ym)) {
                if ("income".equals(t.type)) {
                    income += t.amount;
                } else if ("expense".equals(t.type)) {
                    expense += t.amount;
                    categoryTotals.put(t.subcategory,
                            categoryTotals.getOrDefault(t.subcategory, 0.0) + t.amount);
                }
            }

            System.out.println("\nMonth: " + ym);
            System.out.printf("Total Income : ₹%.2f\n", income);
            System.out.printf("Total Expense: ₹%.2f\n", expense);
            System.out.printf("Net Savings  : ₹%.2f\n", (income - expense));

            System.out.println("Expense Breakdown:");
            for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
                System.out.printf("- %-10s: ₹%.2f\n", entry.getKey(), entry.getValue());
            }
        }
    }

}
