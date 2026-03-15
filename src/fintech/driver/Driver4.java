package fintech.driver;

import fintech.model.*;
import java.util.*;

public class Driver4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<String, Account> accounts = new HashMap<>();
        List<Transaction> allTransactions = new ArrayList<>();
        String line;
        int transactionId = 1;
        
        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            
            if (line.equals("---")) {
                break;
            }
            
            String[] parts = line.split("#");
            
            if (line.startsWith("create-account#")) {
                if (parts.length == 3) {
                    String name = parts[1];
                    String username = parts[2];
                    Account account = new Account(name, username);
                    accounts.put(username, account);
                }
            } else if (line.startsWith("deposit#")) {
                if (parts.length == 5) {
                    String username = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String timestamp = parts[3];
                    String description = parts[4];
                    
                    Account account = accounts.get(username);
                    if (account != null) {
                        account.deposit(amount);
                        DepositTransaction transaction = new DepositTransaction(transactionId++, username, amount, timestamp, description);
                        account.addTransaction(transaction);
                        allTransactions.add(transaction);
                    }
                }
            } else if (line.startsWith("withdraw#")) {
                if (parts.length == 5) {
                    String username = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    String timestamp = parts[3];
                    String description = parts[4];
                    
                    Account account = accounts.get(username);
                    if (account != null) {
                        try {
                            account.withdraw(amount);
                            WithdrawTransaction transaction = new WithdrawTransaction(transactionId++, username, amount, timestamp, description);
                            account.addTransaction(transaction);
                            allTransactions.add(transaction);
                        } catch (NegativeBalanceException e) {
                            // Continue execution, don't stop the program
                        }
                    }
                }
            } else if (line.startsWith("transfer#")) {
                if (parts.length == 6) {
                    String sender = parts[1];
                    String receiver = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    String timestamp = parts[4];
                    String description = parts[5];
                    
                    Account senderAccount = accounts.get(sender);
                    Account receiverAccount = accounts.get(receiver);
                    
                    if (senderAccount != null && receiverAccount != null) {
                        try {
                            senderAccount.transfer(amount, receiverAccount);
                            TransferTransaction transaction = new TransferTransaction(transactionId++, sender, receiver, amount, timestamp, description);
                            senderAccount.addTransaction(transaction);
                            receiverAccount.addTransaction(transaction);
                            allTransactions.add(transaction);
                        } catch (NegativeBalanceException e) {
                            // Continue execution, don't stop the program
                        }
                    }
                }
            } else if (line.startsWith("show-account#")) {
                if (parts.length == 2) {
                    String username = parts[1];
                    Account account = accounts.get(username);
                    if (account != null) {
                        System.out.println(account.toString());
                        
                        // Get transactions for this account and sort by timestamp
                        List<Transaction> accountTransactions = account.getTransactions();
                        accountTransactions.sort(new Comparator<Transaction>() {
                            @Override
                            public int compare(Transaction t1, Transaction t2) {
                                return t1.getTimestamp().compareTo(t2.getTimestamp());
                            }
                        });
                        
                        // Print transactions
                        for (Transaction transaction : accountTransactions) {
                            System.out.println(transaction.toString());
                        }
                    }
                }
            }
        }
        
        scanner.close();
    }
}