package screens;

import database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginGui extends JFrame {
    private TitleScreenGui originalTitleScreen;
    public AdminLoginGui(TitleScreenGui originalTitleScreen) {
        setTitle("Admin Login");
        this.originalTitleScreen = originalTitleScreen;
        setSize(400, 230);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());

        // Color scheme
        Color primaryColor = Color.decode("#FFA500");
        Color textColor = Color.BLACK;
        Font font = new Font("Segoe UI", Font.PLAIN, 16);

        getContentPane().setBackground(primaryColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(textColor);
        userLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        JTextField userField = new JTextField(15);
        userField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(textColor);
        passLabel.setFont(font);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setFont(font);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setFont(font);
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(primaryColor);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        // Optional: Button hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(30, 30, 30));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(Color.BLACK);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(AdminLoginGui.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean isAdmin = JDBC.isAdmin(username, password);
                if (isAdmin) {
                    JOptionPane.showMessageDialog(AdminLoginGui.this, "Access granted. Welcome, Admin!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    CreateQuestionScreenGui createScreen = new CreateQuestionScreenGui(originalTitleScreen);
                    createScreen.setLocationRelativeTo(AdminLoginGui.this);
                    dispose();
                    createScreen.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(AdminLoginGui.this, "Access denied. Admin credentials required.", "Permission Denied", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }
}
