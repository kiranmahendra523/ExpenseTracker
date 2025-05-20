import java.time.LocalDate;

public class Transaction {
    String type;
    String subcategory;
    double amount;
    LocalDate date;

    Transaction(String type, String subcategory, double amount, LocalDate date) {
        this.type = type;
        this.subcategory = subcategory;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type='" + type + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
