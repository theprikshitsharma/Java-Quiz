package screens;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.*;
import static database.JDBC.*;

public class LoginSignupScreen extends JFrame {
    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField signupUsernameField;
    private JPasswordField signupPasswordField;
    private JTextField signupEmailField; // New email field for sign up

    public LoginSignupScreen() {
        setTitle("Login / Sign Up");
        setSize(400, 320); // Increased height to accommodate email field
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Modern Color Scheme
        Color background = new Color(34, 34, 34);
        Color inputBackground = new Color(60, 60, 60);
        Color inputForeground = Color.WHITE;
        Color accent = new Color(0xFFA500);
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(font);
        tabs.setForeground(Color.WHITE);
        tabs.setBackground(background);

        // Login Panel
        JPanel loginPanel = createStyledPanel(background , 4 , 2);
        loginUsernameField = createStyledField(inputBackground, inputForeground, font);
        loginPasswordField = createStyledPasswordField(inputBackground, inputForeground, font);
        JButton loginButton = createStyledButton("Login", accent, font);

        loginPanel.add(new JLabel("Username:")).setForeground(Color.WHITE);
        loginPanel.add(loginUsernameField);
        loginPanel.add(new JLabel("Password:")).setForeground(Color.WHITE);
        loginPanel.add(loginPasswordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);


        // Signup Panel
        JPanel signupPanel = createStyledPanel(background , 5 , 3);
        signupUsernameField = createStyledField(inputBackground, inputForeground, font);
        signupPasswordField = createStyledPasswordField(inputBackground, inputForeground, font);
        signupEmailField = createStyledField(inputBackground, inputForeground, font); // Email field for sign-up
        JButton addEmailButton = createStyledButton("Add Email", accent, font); // Button to add email
        JButton signupButton = createStyledButton("Sign Up", accent, font);

        signupPanel.add(new JLabel("Username:")).setForeground(Color.WHITE);
        signupPanel.add(signupUsernameField);
        signupPanel.add(new JLabel("Password:")).setForeground(Color.WHITE);
        signupPanel.add(signupPasswordField);
        signupPanel.add(new JLabel("Email:")).setForeground(Color.WHITE); // Label for email
        signupPanel.add(signupEmailField); // Add email field
        signupPanel.add(addEmailButton); // Add email button
        signupPanel.add(signupButton);

        // Add tabs
        tabs.add("Login", loginPanel);
        tabs.add("Sign Up", signupPanel);

        // Main panel background
        getContentPane().setBackground(background);
        add(tabs, BorderLayout.CENTER);

        // Action Listeners
        loginButton.addActionListener(e -> login());
        signupButton.addActionListener(e -> signup());
        addEmailButton.addActionListener(e -> addEmail(signupEmailField.getText())); // Add email action

        setVisible(true);
    }

    private void addEmail(String email) {
        String username = signupUsernameField.getText(); // Using signup username for email input

        if (username.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and email.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_db", DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE users SET email = ? WHERE username = ?")) {

            stmt.setString(1, email);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Email added/updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "This email is already used by another account.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private JPanel createStyledPanel(Color bg , int row , int col) {
        JPanel panel = new JPanel(new GridLayout(row, col, 15, 15)); // Increased grid rows to accommodate email
        panel.setBackground(bg);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JTextField createStyledField(Color bg, Color fg, Font font) {
        JTextField field = new JTextField();
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new LineBorder(Color.GRAY, 1, true));
        field.setFont(font);
        return field;
    }

    private JPasswordField createStyledPasswordField(Color bg, Color fg, Font font) {
        JPasswordField field = new JPasswordField();
        field.setBackground(bg);
        field.setForeground(fg);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new LineBorder(Color.GRAY, 1, true));
        field.setFont(font);
        return field;
    }

    private JButton createStyledButton(String text, Color bg, Font font) {
        JButton button = new JButton(text);
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void login() {
        String username = loginUsernameField.getText();
        String password = new String(loginPasswordField.getPassword());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_db", DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                dispose();
                new TitleScreenGui().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }

    private void signup() {
        String username = signupUsernameField.getText();
        String password = new String(signupPasswordField.getPassword());

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_db", DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created successfully!");
        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage());
        }
    }
}
