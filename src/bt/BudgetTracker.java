package bt;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;

public class BudgetTracker extends JFrame {
    private static Map<String, Double> incomeCategories = new HashMap<>();
    private static Map<String, Double> expenseCategories = new HashMap<>();
    private static double budget;
    private static final String INCOME_CATEGORIES_FILE = "incomeCategories.txt";
    private static final String EXPENSE_CATEGORIES_FILE = "expenseCategories.txt";
    private static final String BUDGET_FILE = "budget.txt";

    private static JButton addIncomeButton, addExpenseButton, viewBudgetButton, viewIncomeCategoriesButton, viewExpenseCategoriesButton, addIncomeCategoryButton, addExpenseCategoryButton, exitButton;
    private static JLabel budgetLabel, incomeLabel, expenseLabel;
    private static JTextField incomeAmountField, expenseAmountField;
	private static JTextField incomeCategoryField;
	private static JTextField expenseCategoryField;
    private static JComboBox<String> incomeCategoryComboBox, expenseCategoryComboBox;

    public BudgetTracker() {
        super("Budget Tracker");

        // load income categories, expense categories and budget from file
        loadData();

        // create buttons
        addIncomeButton = new JButton("Add Income");
        addExpenseButton = new JButton("Add Expense");
        viewBudgetButton = new JButton("View Budget");
        viewIncomeCategoriesButton = new JButton("View Income Categories");
        viewExpenseCategoriesButton = new JButton("View Expense Categories");
        addIncomeCategoryButton = new JButton("Add Income Category");
        addExpenseCategoryButton = new JButton("Add Expense Category");
        exitButton = new JButton("Exit");

        // create labels
        budgetLabel = new JLabel("Current Budget: $" + budget);
        incomeLabel = new JLabel("Income:");
        expenseLabel = new JLabel("Expense:");

        // create text fields
        incomeAmountField = new JTextField(10);
        expenseAmountField = new JTextField(10);
        incomeCategoryField = new JTextField(10);
        expenseCategoryField = new JTextField(10);

        // create combo boxes
        incomeCategoryComboBox = new JComboBox<>(incomeCategories.keySet().toArray(new String[0]));
        expenseCategoryComboBox = new JComboBox<>(expenseCategories.keySet().toArray(new String[0]));

        // add action listeners to buttons
        addIncomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addIncome();
            }
        });
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpense();
            }
        });
        viewBudgetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBudget();
            }
        });
        viewIncomeCategoriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewIncomeCategories();
            }
        });
        viewExpenseCategoriesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewExpenseCategories();
            }
        });
        addIncomeCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addIncomeCategory();
            }
        });
        addExpenseCategoryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addExpenseCategory();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
                System.exit(0);
            }
        });

        // create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // create budget panel
        JPanel budgetPanel = new JPanel();
        budgetPanel.add(budgetLabel);

        // create income panel
        JPanel incomePanel = new JPanel();
        incomePanel.setLayout(new FlowLayout());
        incomePanel.add(incomeLabel);
        incomePanel.add(incomeAmountField);
        incomePanel.add(incomeCategoryField);
        incomePanel.add(incomeCategoryComboBox);
        incomePanel.add(addIncomeButton);

        // create expense panel
        JPanel expensePanel = new JPanel();
        expensePanel.setLayout(new FlowLayout());
        expensePanel.add(expenseLabel);
        expensePanel.add(expenseAmountField);
        expensePanel.add(expenseCategoryField);
        expensePanel.add(expenseCategoryComboBox);
        expensePanel.add(addExpenseButton);

        // create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(viewBudgetButton);
        buttonPanel.add(viewIncomeCategoriesButton);
        buttonPanel.add(viewExpenseCategoriesButton);
        buttonPanel.add(addIncomeCategoryButton);
        buttonPanel.add(addExpenseCategoryButton);
        buttonPanel.add(exitButton);

        // add panels to main panel
        mainPanel.add(budgetPanel);
        mainPanel.add(incomePanel);
        mainPanel.add(expensePanel);
        mainPanel.add(buttonPanel);

        // add main panel to frame
        add(mainPanel);

        // set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension center = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(1000, 400);
		setLocation(center.width / 2 - getSize().width / 2, center.height / 2 - getSize().height / 2);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static void addIncome() {
        try {
            double amount = NumberFormat.getInstance().parse(incomeAmountField.getText()).doubleValue();
            String category = incomeCategoryField.getText();
            if (category.isEmpty()) {
                category = (String)incomeCategoryComboBox.getSelectedItem();
            }
            if (incomeCategories.containsKey(category)) {
                incomeCategories.put(category, incomeCategories.get(category) + amount);
                budget += amount;
                updateIncomeCategoryComboBox();
                updateBudgetLabel();
                JOptionPane.showMessageDialog(null, "Income added successfully to category " + category);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid category. Please add a new category or select an existing one.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid amount.");
        }
    }

    private static void addExpense() {
        try {
            double amount = NumberFormat.getInstance().parse(expenseAmountField.getText()).doubleValue();
            String category = expenseCategoryField.getText();
            if (category.isEmpty()) {
                category = (String)expenseCategoryComboBox.getSelectedItem();
            }
            if (expenseCategories.containsKey(category)) {
                expenseCategories.put(category, expenseCategories.get(category) + amount);
                budget -= amount;
                updateExpenseCategoryComboBox();
                updateBudgetLabel();
                JOptionPane.showMessageDialog(null, "Expense added successfully to category " + category);
            } else {
                JOptionPane.showMessageDialog(null, "Invalid category. Please add a new category or select an existing one.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid amount.");
        }
    }

    private static void viewBudget() {
        JOptionPane.showMessageDialog(null, "Current budget: $" + budget);
    }

    private static void viewIncomeCategories() {
        String message = "Income Categories:\n";
        for (Map.Entry<String, Double> entry : incomeCategories.entrySet()) {
            message += entry.getKey() + ": $" + entry.getValue() + "\n";
        }
        JOptionPane.showMessageDialog(null, message);
    }

    private static void viewExpenseCategories() {
        String message = "Expense Categories:\n";
        for (Map.Entry<String, Double> entry : expenseCategories.entrySet()) {
            message += entry.getKey() + ": $" + entry.getValue() + "\n";
        }
        JOptionPane.showMessageDialog(null, message);
    }

    private static void addIncomeCategory() {
        String category = JOptionPane.showInputDialog(null, "Enter new income category:");
        if (!incomeCategories.containsKey(category)) {
            incomeCategories.put(category, 0.0);
            updateIncomeCategoryComboBox();
            JOptionPane.showMessageDialog(null, "Income category added successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Category already exists. Please enter a different category.");
        }
    }

    private static void addExpenseCategory() {
        String category = JOptionPane.showInputDialog(null, "Enter new expense category:");
        if (!expenseCategories.containsKey(category)) {
            expenseCategories.put(category, 0.0);
            updateExpenseCategoryComboBox();
            JOptionPane.showMessageDialog(null, "Expense category added successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Category already exists. Please enter a different category.");
        }
    }

    private static void loadData() {
        try {
            FileInputStream fis = new FileInputStream(INCOME_CATEGORIES_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            incomeCategories = (Map<String, Double>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading income categories from file.");
        }

        try {
            FileInputStream fis = new FileInputStream(EXPENSE_CATEGORIES_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            expenseCategories = (Map<String, Double>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading expense categories from file.");
        }

        try {
            FileInputStream fis = new FileInputStream(BUDGET_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            budget = (double) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading budget from file.");
        }
    }

    private static void saveData() {
        try {
            FileOutputStream fos = new FileOutputStream(INCOME_CATEGORIES_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(incomeCategories);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving income categories to file.");
        }

        try {
            FileOutputStream fos = new FileOutputStream(EXPENSE_CATEGORIES_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(expenseCategories);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving expense categories to file.");
        }

        try {
            FileOutputStream fos = new FileOutputStream(BUDGET_FILE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(budget);
            oos.flush();
            oos.close();
            fos.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving budget to file.");
        }
    }

    private static void updateIncomeCategoryComboBox() {
        incomeCategoryComboBox.removeAllItems();
        for (String category : incomeCategories.keySet()) {
            incomeCategoryComboBox.addItem(category);
        }
    }

    private static void updateExpenseCategoryComboBox() {
        expenseCategoryComboBox.removeAllItems();
        for (String category : expenseCategories.keySet()) {
            expenseCategoryComboBox.addItem(category);
        }
    }

    private static void updateBudgetLabel() {
        budgetLabel.setText("Current Budget: $" + budget);
    }
    public static void main(String[] args) {
        new BudgetTracker();
    }
}







            
