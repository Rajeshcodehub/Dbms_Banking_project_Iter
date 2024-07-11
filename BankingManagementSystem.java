//Name - Rajesh Kumar Pradhan && Reg_NO-2141016180

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BankingManagementSystem {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "oracle49";

        Connection connection = null;
        Statement statement = null;
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            statement = connection.createStatement();
            System.out.println("Connection successful!");

            while (true) {
                System.out.println("\n***** Banking Management System *****");
                System.out.println("1. Show Customer Records");
                System.out.println("2. Add Customer Record");
                System.out.println("3. Delete Customer Record");
                System.out.println("4. Update Customer Information");
                System.out.println("5. Show Account Details of a Customer");
                System.out.println("6. Show Loan Details of a Customer");
                System.out.println("7. Deposit Money to an Account");
                System.out.println("8. Withdraw Money from an Account");
                System.out.println("9. Exit the Program");
                System.out.print("Enter your choice (1-9): ");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        showCustomerRecords(statement);
                        break;
                    case 2:
                        addCustomerRecord(statement, scanner);
                        break;
                    case 3:
                        deleteCustomerRecord(statement, scanner);
                        break;
                    case 4:
                        updateCustomerRecord(statement, scanner);
                        break;
                    case 5:
                        showAccountDetails(statement, scanner);
                        break;
                    case 6:
                        showLoanDetails(statement, scanner);
                        break;
                    case 7:
                        depositMoney(statement, scanner);
                        break;
                    case 8:
                        withdrawMoney(statement, scanner);
                        break;
                    case 9:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static void showCustomerRecords(Statement stmt) throws SQLException {
        String query = "SELECT * FROM Customer";
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            System.out.println("Customer No: " + rs.getString("cust_no") +
                    ", Name: " + rs.getString("name") +
                    ", Phone: " + rs.getString("phoneno") +
                    ", City: " + rs.getString("city"));
        }
        rs.close();
    }

    public static void addCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No: ");
        String custNo = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Phone No: ");
        String phone = scanner.nextLine();
        System.out.print("Enter City: ");
        String city = scanner.nextLine();

        String query = "INSERT INTO Customer (cust_no, name, phoneno, city) VALUES ('" + custNo + "', '" + name + "', '" + phone + "', '" + city + "')";
        stmt.executeUpdate(query);
        System.out.println("Customer record added successfully.");
    }

    public static void deleteCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No to delete: ");
        String custNo = scanner.nextLine();

        String query = "DELETE FROM Customer WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record deleted successfully.");
    }
    public static void updateCustomerRecord(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No to update: ");
        String custNo = scanner.nextLine();
        System.out.println("Enter 1: For Name, 2: For Phone No, 3: For City");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String column = "";
        switch (choice) {
            case 1:
                column = "name";
                break;
            case 2:
                column = "phoneno";
                break;
            case 3:
                column = "city";
                break;
            default:
                System.out.println("Invalid choice! Returning to main menu.");
                return;
        }

        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine().trim();

        String query = "UPDATE Customer SET " + column + " = '" + newValue + "' WHERE cust_no = '" + custNo + "'";
        stmt.executeUpdate(query);
        System.out.println("Customer record updated successfully.");
    }
    public static void showAccountDetails(Statement stmt, Scanner scanner) throws SQLException {
        try {
            System.out.print("Enter Customer No: ");
            String custNo = scanner.nextLine().trim();

            // Check if the customer exists
            String customerQuery = "SELECT * FROM Customer WHERE cust_no = '" + custNo + "'";
            ResultSet customerResult = stmt.executeQuery(customerQuery);
            if (!customerResult.next()) {
                System.out.println("No customer found with the given customer number.");
                return;
            }

            // If customer exists, prompt to add account
            System.out.println("Customer found: " + customerResult.getString("name"));
            System.out.print("Do you want to add an account for this customer? (yes/no): ");
            String addAccountChoice = scanner.nextLine().trim();

            if (addAccountChoice.equalsIgnoreCase("yes")) {
                System.out.println("Adding Account for Customer No: " + custNo);
                System.out.print("Enter Account No: ");
                String accountNo = scanner.nextLine().trim();
                System.out.print("Enter Account Type: ");
                String accountType = scanner.nextLine().trim();
                System.out.print("Enter Initial Balance: ");
                double initialBalance = scanner.nextDouble();
                scanner.nextLine();  // Consume newline
                System.out.print("Enter Branch Code: ");
                String branchCode = scanner.nextLine().trim();
                System.out.print("Enter Branch Name: ");
                String branchName = scanner.nextLine().trim();
                System.out.print("Enter Branch City: ");
                String branchCity = scanner.nextLine().trim();

                // Insert account details into the database
                String query = "INSERT INTO Accounts (account_no, type, balance, branch_code, branch_name, branch_city, cust_no) VALUES ('" +
                        accountNo + "', '" + accountType + "', " + initialBalance + ", '" + branchCode + "', '" + branchName + "', '" +
                        branchCity + "', '" + custNo + "')";
                stmt.executeUpdate(query);
                System.out.println("Account added successfully.");
            } else {
                System.out.println("No account added.");
            }

            // Show existing account details for the customer
            String query = "SELECT * FROM Accounts WHERE cust_no = '" + custNo + "'";
            ResultSet rs = stmt.executeQuery(query);

            boolean hasRecords = false;
            while (rs.next()) {
                hasRecords = true;
                System.out.println("Account No: " + rs.getString("account_no") +
                        ", Type: " + rs.getString("type") +
                        ", Balance: " + rs.getDouble("balance") +
                        ", Branch Code: " + rs.getString("branch_code") +
                        ", Branch Name: " + rs.getString("branch_name") +
                        ", Branch City: " + rs.getString("branch_city"));
            }

            if (!hasRecords) {
                System.out.println("No account details found for the given customer number.");
            }

            rs.close();
        } catch (SQLException e) {
            System.out.println("Failed to retrieve account details: " + e.getMessage());
        }
    }


    public static void showLoanDetails(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Customer No: ");
        String custNo = scanner.nextLine().trim();

        String query = "SELECT * FROM Loan WHERE cust_no = '" + custNo + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (!rs.next()) {
            System.out.println("No loan details found for the given customer number.");

            // If no loan details found, prompt to add loan details
            System.out.println("Do you want to add loan details for this customer? (yes/no)");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("yes")) {
                // Add loan details
                addLoanDetails(stmt, scanner, custNo);
            }
        } else {
            do {
                System.out.println("Loan No: " + rs.getString("loan_no") +
                        ", Loan Amount: " + rs.getDouble("loan_amount") +
                        ", Branch Code: " + rs.getString("branch_code") +
                        ", Branch Name: " + rs.getString("branch_name") +
                        ", Branch City: " + rs.getString("branch_city"));
            } while (rs.next());
        }
        rs.close();
    }

    public static void addLoanDetails(Statement stmt, Scanner scanner, String custNo) throws SQLException {
        System.out.print("Enter Loan No: ");
        String loanNo = scanner.nextLine().trim();
        System.out.print("Enter Loan Amount: ");
        double loanAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Branch Code: ");
        String branchCode = scanner.nextLine().trim();
        System.out.print("Enter Branch Name: ");
        String branchName = scanner.nextLine().trim();
        System.out.print("Enter Branch City: ");
        String branchCity = scanner.nextLine().trim();

        // Insert loan details into the database
        String insertQuery = "INSERT INTO Loan (loan_no, cust_no, loan_amount, branch_code, branch_name, branch_city) " +
                "VALUES ('" + loanNo + "', '" + custNo + "', " + loanAmount + ", '" + branchCode + "', '" + branchName + "', '" + branchCity + "')";
        int rowsInserted = stmt.executeUpdate(insertQuery);
        if (rowsInserted > 0) {
            System.out.println("Loan details added successfully.");
        } else {
            System.out.println("Failed to add loan details.");
        }
    }

    public static void depositMoney(Statement stmt, Scanner scanner) throws SQLException {
        System.out.print("Enter Account No: ");
        String accountNo = scanner.nextLine().trim();

        // Check if the account exists
        String checkAccountQuery = "SELECT * FROM Accounts WHERE account_no = '" + accountNo + "'";
        ResultSet accountRs = stmt.executeQuery(checkAccountQuery);
        if (accountRs.next()) {
            // Account exists, proceed with deposit
            System.out.print("Enter Amount to Deposit: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            String updateQuery = "UPDATE Accounts SET balance = balance + " + amount + " WHERE account_no = '" + accountNo + "'";
            int rowsUpdated = stmt.executeUpdate(updateQuery);
            if (rowsUpdated > 0) {
                System.out.println("Money deposited successfully.");
            } else {
                System.out.println("Failed to deposit money.");
            }
        } else {
            // Account doesn't exist
            System.out.println("Account not found.");
        }
        accountRs.close();
    }

    public static void withdrawMoney(Statement stmt, Scanner scanner) throws SQLException {
            System.out.print("Enter Account No: ");
            String accountNo = scanner.nextLine();
            System.out.print("Enter Amount to Withdraw: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();  // Consume newline

            String checkBalanceQuery = "SELECT balance FROM Accounts WHERE account_no = '" + accountNo + "'";
            System.out.println("Executing query: " + checkBalanceQuery); // Debugging statement
            ResultSet rs = stmt.executeQuery(checkBalanceQuery);

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Current balance: " + balance); // Debugging statement
                if (balance >= amount) {
                    String query = "UPDATE Accounts SET balance = balance - " + amount + " WHERE account_no = '" + accountNo + "'";
                    System.out.println("Executing update query: " + query); // Debugging statement
                    int rowsUpdated = stmt.executeUpdate(query);
                    if (rowsUpdated > 0) {
                        System.out.println("Money withdrawn successfully.");
                    } else {
                        System.out.println("Failed to withdraw money.");
                    }
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
            rs.close();
    }



}