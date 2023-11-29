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

public class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 1));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(15);
        userPanel.add(usernameLabel);
        userPanel.add(usernameField);

        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        passPanel.add(passwordLabel);
        passPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                // Add your authentication logic here
                if (username.equals("admin") && password.equals("password")) {
                    openInventoryManagementSystem();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Try again.");
                }
            }
        });

        loginPanel.add(userPanel);
        loginPanel.add(passPanel);
        loginPanel.add(loginButton);

        add(loginPanel, BorderLayout.CENTER);
    }

    private void openInventoryManagementSystem() {
        InventoryManagementSystemGUI inventorySystem = new InventoryManagementSystemGUI();
        inventorySystem.setVisible(true);
        dispose(); // Close the login window after successful login
    }
}

