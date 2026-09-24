import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class GUI implements ActionListener {
    private CardLayout cardLayout;
    private JPanel cards;
    private JFrame frame;

    private static final String CARD_START = "START";
    private static final String CARD_SECOND = "SECOND";
    private static final String CARD_THIRD = "THIRD";

    private String bankName = "Targobank";

    // Shared fields
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JLabel submitErrorLabel = new JLabel();
    private JLabel balanceLabel = new JLabel("Balance: ");

    // Components we resize dynamically
    private JLabel startWelcome1, startWelcome2;
    private JButton startButton;
    private JLabel loginTitle, loginSubtitle;
    private JButton back, finishInput, registerButton;
    private JButton transferButton, depositButton, withdrawButton, logOutButton;
    private JLabel accountBankLabel;

    private BankService bankService;
    private BankAccount currentAccount;

    public GUI(BankService bankservice) {
        this.bankService = bankservice;
        this.frame = new JFrame("Banking System");
        buildUI();
    }

    private void buildUI() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // ---------- START PANEL ----------
        JPanel startPanel = new JPanel(new GridBagLayout());
        startPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        GridBagConstraints gbcStart = new GridBagConstraints();
        gbcStart.insets = new Insets(20, 20, 20, 20);
        gbcStart.gridx = 0;
        gbcStart.fill = GridBagConstraints.CENTER;

        startWelcome1 = new JLabel("Welcome to", SwingConstants.CENTER);
        startWelcome1.setForeground(new Color(0, 83, 184));
        gbcStart.gridy = 0;
        startPanel.add(startWelcome1, gbcStart);

        startWelcome2 = new JLabel(bankName, SwingConstants.CENTER);
        startWelcome2.setForeground(new Color(0, 22, 48));
        gbcStart.gridy = 1;
        startPanel.add(startWelcome2, gbcStart);

        startButton = new JButton("Enter Banking");
        startButton.setActionCommand("SECOND");
        startButton.addActionListener(this);
        gbcStart.gridy = 2;
        startPanel.add(startButton, gbcStart);

        cards.add(startPanel, CARD_START);

        // ---------- LOGIN PANEL ----------
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        loginTitle = new JLabel("BrostBank", SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(loginTitle, gbc);

        loginSubtitle = new JLabel("Log in", SwingConstants.CENTER);
        gbc.gridy = 1;
        loginPanel.add(loginSubtitle, gbc);

        submitErrorLabel.setForeground(Color.RED);
        submitErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        loginPanel.add(submitErrorLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 3; gbc.gridx = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        finishInput = new JButton("Submit");
        finishInput.setActionCommand("SUBMIT_LOGIN_INPUT");
        finishInput.addActionListener(this);

        // gabs vorher gar nicht, ohne den kann man sich nie einloggen weil nie ein customer existiert
        registerButton = new JButton("Register");
        registerButton.setActionCommand("REGISTER");
        registerButton.addActionListener(this);

        back = new JButton("Back");
        back.setActionCommand("BACK_START");
        back.addActionListener(this);

        JPanel buttonPanelLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanelLogin.add(back);
        buttonPanelLogin.add(registerButton);
        buttonPanelLogin.add(finishInput);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        loginPanel.add(buttonPanelLogin, gbc);

        usernameField.addActionListener(_ -> finishInput.doClick());
        passwordField.addActionListener(_ -> finishInput.doClick());

        cards.add(loginPanel, CARD_SECOND);

        // ---------- MANAGE ACCOUNT PANEL ----------
        JPanel manageAccountPanel = new JPanel(new BorderLayout(20, 20));
        manageAccountPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel topPanel = new JPanel(new BorderLayout());
        accountBankLabel = new JLabel("BrostBank");
        topPanel.add(accountBankLabel, BorderLayout.WEST);

        balanceLabel = new JLabel("Balance: ");
        topPanel.add(balanceLabel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        transferButton = new JButton("Transfer");
        transferButton.setActionCommand("TRANSFER");
        transferButton.addActionListener(this);

        depositButton = new JButton("Deposit");
        depositButton.setActionCommand("DEPOSIT");
        depositButton.addActionListener(this);

        withdrawButton = new JButton("Withdraw");
        withdrawButton.setActionCommand("WITHDRAW");
        withdrawButton.addActionListener(this);

        buttonPanel.add(transferButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);

        logOutButton = new JButton("Log out");
        logOutButton.setActionCommand("LOGOUT");
        logOutButton.addActionListener(this);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(logOutButton);

        manageAccountPanel.add(topPanel, BorderLayout.NORTH);
        manageAccountPanel.add(buttonPanel, BorderLayout.CENTER);
        manageAccountPanel.add(bottomPanel, BorderLayout.SOUTH);

        cards.add(manageAccountPanel, CARD_THIRD);

        // ---------- FRAME ----------
        frame.add(cards, BorderLayout.CENTER);
        cardLayout.show(cards, CARD_START);

        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Attach scaling
        addScalingListener();
    }

    private void addScalingListener() {
        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = frame.getWidth();
                int h = frame.getHeight();
                int base = Math.min(w, h);

                float bigFont = base / 20f;     // titles
                float mediumFont = base / 30f;  // subtitles / balance
                float smallFont = base / 40f;   // fields
                float buttonFont = base / 35f;  // buttons

                // ---------- Fonts ----------
                startWelcome1.setFont(startWelcome1.getFont().deriveFont(mediumFont));
                startWelcome2.setFont(startWelcome2.getFont().deriveFont(bigFont));
                startButton.setFont(startButton.getFont().deriveFont(buttonFont));

                loginTitle.setFont(loginTitle.getFont().deriveFont(bigFont));
                loginSubtitle.setFont(loginSubtitle.getFont().deriveFont(mediumFont));
                usernameField.setFont(usernameField.getFont().deriveFont(smallFont));
                passwordField.setFont(passwordField.getFont().deriveFont(smallFont));
                finishInput.setFont(finishInput.getFont().deriveFont(buttonFont));
                back.setFont(back.getFont().deriveFont(buttonFont));
                registerButton.setFont(registerButton.getFont().deriveFont(buttonFont));

                accountBankLabel.setFont(accountBankLabel.getFont().deriveFont(mediumFont));
                balanceLabel.setFont(balanceLabel.getFont().deriveFont(mediumFont));
                transferButton.setFont(transferButton.getFont().deriveFont(buttonFont));
                depositButton.setFont(depositButton.getFont().deriveFont(buttonFont));
                withdrawButton.setFont(withdrawButton.getFont().deriveFont(buttonFont));
                logOutButton.setFont(logOutButton.getFont().deriveFont(buttonFont));

                // ---------- Component sizes ----------
                int fieldWidth = base / 3;
                int fieldHeight = base / 25;
                usernameField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
                passwordField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));

                int buttonWidth = base / 5;
                int buttonHeight = base / 15;
                startButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                transferButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                depositButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                withdrawButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                logOutButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                finishInput.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                back.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
                registerButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));

                // ---------- Adjust panel spacing dynamically ----------
                int padding = base / 30;
                cards.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

                frame.revalidate();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        switch(cmd) {
            case "SECOND" -> cardLayout.show(cards, CARD_SECOND);

            case "BACK_START" -> {
                cardLayout.show(cards, CARD_START);
                usernameField.setText("");
                passwordField.setText("");
            }

            case "SUBMIT_LOGIN_INPUT" -> {
                String username = usernameField.getText().trim();
                String pin = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || pin.isEmpty()) {
                    submitErrorLabel.setText("Can't leave empty text fields.");
                    return;
                }

                BankAccount loginAccount = null;
                try {
                    loginAccount = bankService.tryLogin(username, pin);
                } catch (WrongPinException exc) {
                    submitErrorLabel.setText(exc.getMessage());
                }

                if (loginAccount != null) {
                    usernameField.setText("");
                    passwordField.setText("");
                    cardLayout.show(cards, CARD_THIRD);
                    balanceLabel.setText("Balance: " + loginAccount.getBalance() + "€");
                    currentAccount = loginAccount;
                } else {
                    submitErrorLabel.setText("Wrong password or username.");
                }
            }

            case "REGISTER" -> {
                String username = usernameField.getText().trim();
                String pin = new String(passwordField.getPassword()).trim();

                if (username.isEmpty() || pin.isEmpty()) {
                    submitErrorLabel.setText("Can't leave empty text fields.");
                    return;
                }

                if (bankService.getCustomerByName(username) != null) {
                    submitErrorLabel.setText("User already exists.");
                    return;
                }

                Customer newCustomer = bankService.registerCustomer(username, pin);
                currentAccount = newCustomer.getAccount();

                usernameField.setText("");
                passwordField.setText("");
                cardLayout.show(cards, CARD_THIRD);
                balanceLabel.setText("Balance: " + currentAccount.getBalance() + "€");
            }

            case "TRANSFER" -> {
                String input = JOptionPane.showInputDialog(frame, "Customer name: ");
                if (input != null) {
                    BankAccount ba = bankService.getBankAccountbyName(input);

                    if (ba == null) {
                        JOptionPane.showMessageDialog(frame, "Could not find that person.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (ba.equals(currentAccount)) {
                        JOptionPane.showMessageDialog(frame, "Can't send money to yourself.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {
                            BigDecimal amount = new BigDecimal(JOptionPane.showInputDialog(frame, "Transfer amount: "));
                            if (amount.scale() > 2) {
                                JOptionPane.showMessageDialog(frame, "More than two decimal numbers", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                                JOptionPane.showMessageDialog(frame, "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (amount.compareTo(currentAccount.getBalance()) > 0) { // check hat gefehlt, gabs bei withdraw schon
                                JOptionPane.showMessageDialog(frame, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            currentAccount.changeBalance(amount.negate());
                            balanceLabel.setText("Balance: " + String.format("%.2f", currentAccount.getBalance()) + "€");
                            ba.changeBalance(amount);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            case "DEPOSIT" -> {
                String input = JOptionPane.showInputDialog(frame, "Deposit amount:");
                if (input != null) {
                    try {
                        BigDecimal bd = new BigDecimal(input);
                        if (bd.scale() > 2) {
                            JOptionPane.showMessageDialog(frame, "More than two decimal numbers", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (bd.compareTo(new BigDecimal("0.10")) < 0) {
                            JOptionPane.showMessageDialog(frame, "Input too small", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        currentAccount.changeBalance(bd);
                        balanceLabel.setText("Balance: " + String.format("%.2f", currentAccount.getBalance()) + "€");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            case "WITHDRAW" -> {
                String input = JOptionPane.showInputDialog(frame, "Withdraw amount:");
                if (input != null) {
                    try {
                        BigDecimal bd = new BigDecimal(input);
                        if (bd.scale() > 2) {
                            JOptionPane.showMessageDialog(frame, "More than two decimal numbers", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (bd.compareTo(currentAccount.getBalance()) > 0) {
                            JOptionPane.showMessageDialog(frame, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        currentAccount.changeBalance(bd.negate());
                        balanceLabel.setText("Balance: " + String.format("%.2f", currentAccount.getBalance()) + "€");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

            case "LOGOUT" -> {
                bankService.saveCustomers();
                cardLayout.show(cards, CARD_SECOND);
                currentAccount = null;
            }
        }
    }
}
