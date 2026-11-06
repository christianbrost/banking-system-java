# BankingSystem

small banking system in java to learn OOP. swing GUI, customers are stored in a text file.

## run

```
javac *.java
java Main
```

## what works

- register + login (username + pin)
- deposit, withdraw, transfer money to other customers
- data persists after closing (`Customers.txt`)

## what's missing

- transaction history
- own account id / iban
- pin is stored in plaintext in the file, not a real project for real money :)
