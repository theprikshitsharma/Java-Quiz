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
import java.util.ArrayList;
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
        getContentPane().setBackground(CommonConstants.LIGHT_BLUE);
        addGuiComponents();

    }

    private void addGuiComponents() {
        // title label
        JLabel titleLabel = new JLabel("Quiz Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0,20,390,43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        // titleLabel.setOpaque(true);
        // titleLabel.setBackground(CommonConstants.BLACK);
        add(titleLabel);

        // category label
        JLabel chooseCatagoryLabel = new JLabel("Choose a catagory");
        chooseCatagoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCatagoryLabel.setBounds(0,90,400,20);
        chooseCatagoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCatagoryLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(chooseCatagoryLabel);

        // category drop down menu

        ArrayList<String> categoryList = JDBC.getCategories();

        categoriesMenu = new JComboBox(categoryList.toArray());
        categoriesMenu.setBounds(20, 120, 337, 45);
        categoriesMenu.setBackground(CommonConstants.BLACK);
        categoriesMenu.setForeground(CommonConstants.BRIGHT_YELLOW);
        categoriesMenu.setUI(new javax.swing.plaf.metal.MetalComboBoxUI() {
            @Override
            protected ComboPopup createPopup() {
                BasicComboPopup popup = (BasicComboPopup) super.createPopup();
                popup.getList().setBackground(CommonConstants.BLACK);  // Set drop-down background color
                popup.getList().setForeground(CommonConstants.BRIGHT_YELLOW); // Set drop-down text color
                return popup;
            }
        });
        add(categoriesMenu);

        // num of questions label
        JLabel numOfQuestionsLabel = new JLabel("Number of Questions: ");
        numOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsLabel.setBounds(20, 190, 172, 20);
        numOfQuestionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionsLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(numOfQuestionsLabel);

        // num of questions text input field
        numOfQuestionsTextField = new JTextField("10");
        numOfQuestionsTextField.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsTextField.setBounds(200, 190, 148, 26);
        numOfQuestionsTextField.setForeground(CommonConstants.BRIGHT_YELLOW);
        numOfQuestionsTextField.setBackground(CommonConstants.BLACK);
        add(numOfQuestionsTextField);

        // start button
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(65, 290, 262, 45);
        startButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        startButton.setForeground(CommonConstants.BLACK);
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

        // exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBounds(65, 350, 262, 45);
        exitButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        exitButton.setForeground(CommonConstants.LIGHT_BLUE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // dispose of this screen
                TitleScreenGui.this.dispose();
            }
        });
        add(exitButton);

        // create a question button
        JButton createAQuestionButton = new JButton("Create a Question");
        createAQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        createAQuestionButton.setBounds(65, 420, 262, 45);
        createAQuestionButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        createAQuestionButton.setForeground(CommonConstants.LIGHT_BLUE);
        createAQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create question screen gui
                CreateQuestionScreenGui createQuestionScreenGui = new CreateQuestionScreenGui();
                createQuestionScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                // dispose of this title screen
                TitleScreenGui.this.dispose();

                // display create a question screen gui
                createQuestionScreenGui.setVisible(true);


            }
        });
        add(createAQuestionButton);
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
