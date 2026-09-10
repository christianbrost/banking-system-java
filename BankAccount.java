import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;
@SuppressWarnings("unused")


public class BankAccount {
    private BigDecimal balance;
    Scanner scanner = new Scanner(System.in);

    public BankAccount(BigDecimal balance) { //constructor
        this.balance = balance.setScale(2, RoundingMode.HALF_UP);
    }

    public void printBalance() {
        System.out.println("Your current balance is "+this.balance+".");
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void changeBalance(BigDecimal amount) {
        BigDecimal newBalance = balance.add(amount).setScale(2, RoundingMode.HALF_UP);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) { // muss newBalance sein nicht balance, sonst friert das konto für immer ein sobald man einmal ins minus rutscht
            return;
        }

        balance = newBalance;
    }

    public BigDecimal deposit(BigDecimal amount) {
        changeBalance(amount);
        return this.balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        changeBalance(amount.negate());
        return balance; // war vorher balance.subtract(amount), hat den betrag doppelt abgezogen
    }

    public boolean sendMoney(Bank bank, BigDecimal amount, String recipientName) {
        //insufficient balance?
        if(amount.compareTo(balance)>0) {
            return false;
        }

        if((bank.getCustomerAccountByName(recipientName))!= null) {
            bank.getCustomerAccountByName(recipientName).changeBalance(amount);
            this.changeBalance(amount.negate()); //set balance
            //System.out.println("Successfully sent "+amount+ " Euros.");
        }

        return false;
    }
}