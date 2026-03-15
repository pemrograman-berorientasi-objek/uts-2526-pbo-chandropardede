package fintech.driver;

import fintech.model.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Account> accounts = new ArrayList<>();
        String line;
        
        while (scanner.hasNextLine()) {
            line = scanner.nextLine().trim();
            
            if (line.equals("---")) {
                break;
            }
            
            if (line.startsWith("create-account#")) {
                String[] parts = line.split("#");
                if (parts.length == 3) {
                    String name = parts[1];
                    String username = parts[2];
                    Account account = new Account(name, username);
                    accounts.add(account);
                }
            }
        }
        
        // Output all accounts
        for (Account account : accounts) {
            System.out.println(account.toString());
        }
        
        scanner.close();
    }
}