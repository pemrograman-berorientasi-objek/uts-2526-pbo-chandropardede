package fintech.driver;

import fintech.model.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Driver3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LinkedHashMap<String, Account> accounts = new LinkedHashMap<>();
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
                            // Ignore invalid withdrawals that would cause negative balance
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
                            // Ignore invalid transfers that would cause negative balance
                        }
                    }
                }
            }
        }
        
        // Output all accounts
        for (Account account : accounts.values()) {
            System.out.println(account.toString());
        }
        
        scanner.close();
    }
}