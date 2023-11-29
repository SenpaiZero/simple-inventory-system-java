/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop_case_study;

/**
 *
 * @author PC1
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class InventoryManagementSystemGUI extends JFrame {
    private Connection connection;
    private List<InventoryItem> inventory;
    private JTextArea displayArea;
    private JTextField idField;
    private JTextField itemNameField;
    private JTextField quantityField;
    private JTextField costPriceField;
    private JTextField sellingPriceField;

    private double totalSalesCost = 0; // Total cost of sold items

    public InventoryManagementSystemGUI() {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        inventory = new ArrayList<>();

        setTitle("Bakery Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(6, 2));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel idLabel = new JLabel("Product ID:");
        idField = new JTextField();
        idField.setEditable(false); // ID is generated automatically
        inputPanel.add(idLabel);
        inputPanel.add(idField);

        JLabel itemNameLabel = new JLabel("Item Name:");
        itemNameField = new JTextField();
        inputPanel.add(itemNameLabel);
        inputPanel.add(itemNameField);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField();
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);

        JLabel costPriceLabel = new JLabel("Price per Piece:");
        costPriceField = new JTextField();
        inputPanel.add(costPriceLabel);
        inputPanel.add(costPriceField);

        JLabel sellingPriceLabel = new JLabel("Selling Price:");
        sellingPriceField = new JTextField();
        inputPanel.add(sellingPriceLabel);
        inputPanel.add(sellingPriceField);

        JButton addItemToDatabaseButton = new JButton("Add Item");
        
        addItemToDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addItemToDatabase();
            }

        });
        inputPanel.add(new JLabel()); // Empty label for alignment
        inputPanel.add(addItemToDatabaseButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton displayInventoryButton = new JButton("Display Inventory");
        displayInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayInventoryFromDatabase();
            }
        });

        JButton searchItemButton = new JButton("Search Item");
        searchItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchItem();
            }
        });

        JButton sellItemButton = new JButton("Sell Item");
        sellItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellItemFromDatabase();
            }
        });

        buttonPanel.add(displayInventoryButton);
        buttonPanel.add(searchItemButton);
        buttonPanel.add(sellItemButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        try{
        //Connection to SQL database
        String url = "jdbc:mysql://localhost:3306/oop";
        String username = "root";
        String password = "";
        
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to the Database.");
        }catch (SQLException e){
        System.out.println("Connection Failed.");
        e.printStackTrace();
        }
    }

    private void addItemToDatabase() {
        String itemName = itemNameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double costPrice = Double.parseDouble(costPriceField.getText());
        double sellingPrice = Double.parseDouble(sellingPriceField.getText());

        try {
            String sql = "INSERT INTO oop (item_name, quantity, cost_price, selling_price) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, itemName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, costPrice);
            pstmt.setDouble(4, sellingPrice);
            pstmt.executeUpdate();
            displayArea.append("Item added to the database.\n");
        } catch (SQLException e) {
            displayArea.append("Failed to add item to the database.\n");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to add item to the database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 

    private void updateDisplayId() {
         JTextField idField = new JTextField(5);

    JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridLayout(0, 1));
    myPanel.add(new JLabel("Enter Item ID:"));
    myPanel.add(idField);

    int result = JOptionPane.showConfirmDialog(null, myPanel, "Enter ID",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        try {
            int id = Integer.parseInt(idField.getText());

            String sql = "SELECT * FROM oop WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();

            JTextArea displayArea = new JTextArea(20, 40);
            displayArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(displayArea);
            displayArea.setText(""); // Clear previous content

            if (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");
                double costPrice = resultSet.getDouble("cost_price");
                double sellingPrice = resultSet.getDouble("selling_price");

                displayArea.append("ID: " + productId + " - " +
                        itemName + " - Quantity: " + quantity +
                        " - Cost Price: $" + costPrice + " - Selling Price: $" + sellingPrice + "\n");
            } else {
                displayArea.append("Item not found.\n");
            }

            JOptionPane.showMessageDialog(null, scrollPane, "Item Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Invalid input or item not found.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

    private void displayInventoryFromDatabase() {
        JTextArea textArea = new JTextArea(20, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

    try {
        String sql = "SELECT * FROM oop";
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        textArea.setText(""); // Clear previous content
        while (resultSet.next()) {
            int productId = resultSet.getInt("id");
            String itemName = resultSet.getString("item_name");
            int quantity = resultSet.getInt("quantity");
            double costPrice = resultSet.getDouble("cost_price");
            double sellingPrice = resultSet.getDouble("selling_price");

            textArea.append("ID: " + productId + " - " +
                    itemName + " - Quantity: " + quantity +
                    " - Cost Price: $" + costPrice + " - Selling Price: $" + sellingPrice + "\n");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    JOptionPane.showMessageDialog(null, scrollPane, "Inventory", JOptionPane.INFORMATION_MESSAGE);
}
    
    
    private void searchItem() {
        JTextField nameField = new JTextField(20);

    JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridLayout(0, 1));
    myPanel.add(new JLabel("Enter Item Name:"));
    myPanel.add(nameField);

    int result = JOptionPane.showConfirmDialog(null, myPanel, "Search Item",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String itemName = nameField.getText();

        try {
            String sql = "SELECT * FROM oop WHERE item_name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, itemName);
            ResultSet resultSet = pstmt.executeQuery();

            JTextArea displayArea = new JTextArea(20, 40);
            displayArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(displayArea);

            displayArea.setText(""); // Clear previous content

            if (resultSet.next()) {
                int productId = resultSet.getInt("id");
                int quantity = resultSet.getInt("quantity");
                double costPrice = resultSet.getDouble("cost_price");
                double sellingPrice = resultSet.getDouble("selling_price");

                displayArea.append("ID: " + productId + " - " +
                        itemName + " - Quantity: " + quantity +
                        " - Cost Price: $" + costPrice + " - Selling Price: $" + sellingPrice + "\n");
            } else {
                displayArea.append("Item not found.\n");
            }

            JOptionPane.showMessageDialog(null, scrollPane, "Item Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while searching item.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

    private void sellItemFromDatabase() {
    JTextField nameField = new JTextField(20);
    JTextField quantityField = new JTextField(5);

    JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridLayout(0, 1));
    myPanel.add(new JLabel("Item Name:"));
    myPanel.add(nameField);
    myPanel.add(new JLabel("Quantity to Sell:"));
    myPanel.add(quantityField);

    int result = JOptionPane.showConfirmDialog(null, myPanel, "Sell Item",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String itemName = nameField.getText();
        int sellQuantity = Integer.parseInt(quantityField.getText());

        try {
            String selectSQL = "SELECT quantity, cost_price, selling_price FROM oop WHERE item_name = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
            selectStmt.setString(1, itemName);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                int availableQuantity = resultSet.getInt("quantity");
                double costPrice = resultSet.getDouble("cost_price");
                double sellingPrice = resultSet.getDouble("selling_price");

                if (sellQuantity <= availableQuantity) {
                    double totalCost = costPrice * sellQuantity;
                    double totalSellingPrice = sellingPrice * sellQuantity;
                    double profit = totalSellingPrice - totalCost;

                    String updateSQL = "UPDATE oop SET quantity = ? WHERE item_name = ?";
                    PreparedStatement updateStmt = connection.prepareStatement(updateSQL);
                    updateStmt.setInt(1, availableQuantity - sellQuantity);
                    updateStmt.setString(2, itemName);
                    updateStmt.executeUpdate();

                    displayArea.append("Sold " + sellQuantity + " units of " + itemName + ".\n");
                    displayArea.append("Profit from selling " + sellQuantity + " units: $" + profit + "\n");
                    displayArea.append("Remaining quantity of " + itemName + " in inventory: " + (availableQuantity - sellQuantity) + "\n");
                } else {
                    displayArea.append("Not enough quantity in inventory to sell.\n");
                }
            } else {
                displayArea.append("Item not found in inventory.\n");
            }
        } catch (SQLException e) {
            displayArea.append("Failed to sell item from the database.\n");
            e.printStackTrace();
        }
    }
    }
}