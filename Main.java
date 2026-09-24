import javax.swing.SwingUtilities;

public class Main { //bootsraper
    @SuppressWarnings("unused")
    public static void main(String[] args) throws Exception {
        Bank bank = new Bank("MyBank",5);
        BankService bankService = new BankService(bank);
        
        //debugging
        bankService.loadCustomers();
        bank.printCustomers();
        
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(bankService);
        });
        
    }

    public static void printLoading(){
        try{
            Thread.sleep(700);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(700);
            System.out.print(".\n");
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted.");
        }
    }
}
