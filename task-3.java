
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }
}

class ATM {
    private List<User> users;
    private User currentUser;
    private Scanner scanner;

    public ATM() {
        users = new ArrayList<>();
        users.add(new User("1234", "0000")); // Example user (id: 1234, pin: 0000)
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the ATM!");

        // Prompt for user ID and PIN
        System.out.print("Enter your user ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter your PIN: ");
        String pin = scanner.nextLine();

        // Validate user ID and PIN
        User user = validateUser(userId, pin);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful. Welcome, " + currentUser.getUserId() + "!");
            showMenu();
        } else {
            System.out.println("Invalid user ID or PIN. Exiting...");
        }
    }

    private User validateUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                return user;
            }
        }
        return null;
    }

    private void showMenu() {
        boolean quit = false;

        while (!quit) {
            System.out.println("\n----- ATM Menu -----");
            System.out.println("1. View Transactions History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    viewTransactionHistory();
                    break;
                case 2:
                    withdraw();
                    break;
                case 3:
                    deposit();
                    break;
                case 4:
                    transfer();
                    break;
                case 5:
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewTransactionHistory() {
        List<Transaction> transactions = currentUser.getTransactions();

        System.out.println("\n----- Transaction History -----");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        }
    }

    private void withdraw() {
        System.out.print("Enter the amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        if (currentUser.getAccount().withdraw(amount)) {
            System.out.println("Withdrawal successful. Please take your cash.");
            currentUser.addTransaction(new Transaction("Withdrawal", -amount));
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void deposit() {
        System.out.print("Enter the amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        currentUser.getAccount().deposit(amount);
        System.out.println("Deposit successful.");
        currentUser.addTransaction(new Transaction("Deposit", amount));
    }

    private void transfer() {
        System.out.print("Enter the recipient's user ID: ");
        String recipientId = scanner.nextLine();
        System.out.print("Enter the amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        User recipient = findUserById(recipientId);

        if (recipient != null) {
            if (currentUser.getAccount().withdraw(amount)) {
                recipient.getAccount().deposit(amount);
                System.out.println("Transfer successful.");
                currentUser.addTransaction(new Transaction("Transfer to " + recipient.getUserId(), -amount));
                recipient.addTransaction(new Transaction("Transfer from " + currentUser.getUserId(), amount));
            } else {
                System.out.println("Insufficient balance.");
            }
        } else {
            System.out.println("Recipient not found.");
        }
    }

    private User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}

class User {
    private String userId;
    private String pin;
    private Account account;
    private List<Transaction> transactions;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.account = new Account();
        this.transactions = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}

class Account {
    private double balance;

    public Account() {
        this.balance = 0.0;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        balance += amount;
    }
}

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", Amount: " + amount;
    }
}
