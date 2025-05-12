package screens;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import constants.CommonConstants;
import database.JDBC;
import database.Category;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static database.JDBC.DB_PASSWORD;
import static database.JDBC.DB_USERNAME;
// import java.util.Locale.Category;

public class TitleScreenGui extends JFrame{

    private JComboBox categoriesMenu;
    private JTextField numOfQuestionsTextField;

    public TitleScreenGui() {
        super("Title Screen");
        setSize(400 , 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.BG);
        addGuiComponents();

    }

    private void addGuiComponents() {
        // title label
        JLabel titleLabel = new JLabel("Quiz Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0,20,390,43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(CommonConstants.BLACK);
        // titleLabel.setOpaque(true);
        // titleLabel.setBackground(CommonConstants.BLACK);
        add(titleLabel);

        // category label
        JLabel chooseCatagoryLabel = new JLabel("Choose a catagory");
        chooseCatagoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCatagoryLabel.setBounds(0,90,400,20);
        chooseCatagoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCatagoryLabel.setForeground(CommonConstants.BLACK);
        add(chooseCatagoryLabel);

        // category drop down menu

        ArrayList<String> categoryList = JDBC.getCategories();

        categoriesMenu = new JComboBox(categoryList.toArray());
        categoriesMenu.setBounds(20, 120, 337, 45);
        categoriesMenu.setBackground(CommonConstants.BLACK);
        categoriesMenu.setForeground(CommonConstants.BG);
        categoriesMenu.setUI(new javax.swing.plaf.metal.MetalComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                popup.getList().setBackground(CommonConstants.BG);  // Set drop-down background color
                popup.getList().setForeground(CommonConstants.BLACK); // Set drop-down text color
                return popup;
            }
        });
        add(categoriesMenu);

        // num of questions label
        JLabel numOfQuestionsLabel = new JLabel("Number of Questions: ");
        numOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsLabel.setBounds(20, 190, 172, 20);
        numOfQuestionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionsLabel.setForeground(CommonConstants.BLACK);
        add(numOfQuestionsLabel);

        // num of questions text input field
        numOfQuestionsTextField = new JTextField("10");
        numOfQuestionsTextField.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsTextField.setBounds(200, 190, 148, 26);
        numOfQuestionsTextField.setForeground(CommonConstants.BG);
        numOfQuestionsTextField.setBackground(CommonConstants.BLACK);
        add(numOfQuestionsTextField);

        // start button
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(65, 290, 262, 45);
        startButton.setBackground(CommonConstants.BLACK);
        startButton.setForeground(CommonConstants.BG);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    Category category = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());

                    // ivalid category
                    if (category == null) return;

                    int numOfQuestions = Integer.parseInt(numOfQuestionsTextField.getText());

                    // load quiz screen
                    QuizScreenGui quizScreenGui = new QuizScreenGui(category, numOfQuestions);
                    quizScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                    // dispose of this screen
                    TitleScreenGui.this.dispose();

                    // disply quiz screen
                    quizScreenGui.setVisible(true);
                }
            }
        });
        add(startButton);

        JButton createAQuestionButton = new JButton("Admin Panel");
        createAQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAQuestionButton.setBounds(65, 350, 262, 45);
        createAQuestionButton.setBackground(CommonConstants.BLACK);
        createAQuestionButton.setForeground(CommonConstants.BG);
        createAQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminLoginGui(TitleScreenGui.this);
            }
        });
        add(createAQuestionButton);

        // exit button
//        JButton exitButton = new JButton("Clear Database");
//        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
//        exitButton.setBounds(65, 410, 262, 45);
//        exitButton.setBackground(CommonConstants.BLACK);
//        exitButton.setForeground(CommonConstants.BG);
//        exitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                int confirm = JOptionPane.showConfirmDialog(
//                        TitleScreenGui.this,
//                        "Are you sure you want to clear the entire database?",
//                        "Confirm Clear",
//                        JOptionPane.YES_NO_OPTION
//                );
//
//                if (confirm == JOptionPane.YES_OPTION) {
//                    try (Connection conn = DriverManager.getConnection(
//                            "jdbc:mysql://localhost:3306/quiz_db", DB_USERNAME, DB_PASSWORD);
//                         Statement stmt = conn.createStatement()) {
//
//                        // Disable foreign key checks to allow truncation
//                        stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
//
//                        // Add your table names below
//                        String[] tables = {"answer", "category", "question"};
//                        for (String table : tables) {
//                            stmt.execute("TRUNCATE TABLE " + table);
//                        }
//
//                        stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
//
//                        JOptionPane.showMessageDialog(
//                                TitleScreenGui.this,
//                                "Database cleared successfully.",
//                                "Success",
//                                JOptionPane.INFORMATION_MESSAGE
//                        );
//                    } catch (SQLException ex) {
//                        ex.printStackTrace();
//                        JOptionPane.showMessageDialog(
//                                TitleScreenGui.this,
//                                "Error clearing database: " + ex.getMessage(),
//                                "Error",
//                                JOptionPane.ERROR_MESSAGE
//                        );
//                    }
//                }
//            }
//        });
//
//        add(exitButton);

        JButton clearButton = new JButton("Exit");
        clearButton.setFont(new Font("Arial", Font.BOLD, 16));
        clearButton.setBounds(65, 410, 262, 45);
        clearButton.setBackground(CommonConstants.BLACK);
        clearButton.setForeground(CommonConstants.BG);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // dispose of this screen
                TitleScreenGui.this.dispose();
            }
        });
        add(clearButton);

    }

    // true - valid input
    // false - invalid input

    private boolean validateInput() {
        // num of questions field must not be empty
        if(numOfQuestionsTextField.getText().replaceAll(" ", "").length() <= 0) return false;

        // no category is chosen
        if(categoriesMenu.getSelectedItem() == null) return false;

        return true;
    }

}
