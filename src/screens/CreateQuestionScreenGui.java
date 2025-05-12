package screens;

import javax.swing.*;

import constants.CommonConstants;
import database.JDBC;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CreateQuestionScreenGui extends JFrame{

    private JTextArea questionTextArea;
    private JTextField categoryTextField;
    private JTextField[] answerTextFields;
    private ButtonGroup buttonGroup;
    private JRadioButton[] answerRadioButtons;
    private TitleScreenGui originalTitleScreen;

    public CreateQuestionScreenGui(TitleScreenGui originalTitleScreen)  {
        super("Create a Question");
        this.originalTitleScreen = originalTitleScreen;
        setSize(851 , 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(30, 30, 30));

        answerRadioButtons = new JRadioButton[4];
        answerTextFields = new JTextField[4];
        buttonGroup = new ButtonGroup();

        addGuiComponents();

    }

    public void addGuiComponents() {

        Font modernFont = new Font("Segoe UI", Font.BOLD, 16);
        Font titleFont = new Font("Segoe UI", Font.BOLD, 24);

        JLabel titleLabel = new JLabel("Create your own Question");
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(50,15,310,29);
        titleLabel.setForeground(new Color(0xFFA500));
        add(titleLabel);

        JLabel questionLabel = new JLabel("Question: ");
        questionLabel.setFont(modernFont);
        questionLabel.setBounds(50,60,93,20);
        questionLabel.setForeground(new Color(0xFFA500));
        add(questionLabel);

        questionTextArea = new JTextArea();
        questionTextArea.setFont(modernFont);
        questionTextArea.setBounds(50,90,310,110);
        questionTextArea.setForeground(Color.WHITE);
        questionTextArea.setBackground(new Color(50, 50, 50));
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        add(questionTextArea);

        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(modernFont);
        categoryLabel.setBounds(50,250,93,20);
        categoryLabel.setForeground(new Color(0xFFA500));
        add(categoryLabel);

        categoryTextField = new JTextField();
        categoryTextField.setFont(modernFont);
        categoryTextField.setBounds(50,280,310,36);
        categoryTextField.setForeground(Color.WHITE);
        categoryTextField.setBackground(new Color(50, 50, 50));
        add(categoryTextField);

        // add score here

        JLabel numOfQuestionsLabel = new JLabel("Points: ");
        numOfQuestionsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionsLabel.setBounds(20, 190, 172, 20);
        numOfQuestionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionsLabel.setForeground(CommonConstants.BLACK);
        add(numOfQuestionsLabel);

        // num of questions text input field
//        numOfQuestionsTextField = new JTextField("10");
//        numOfQuestionsTextField.setFont(new Font("Arial", Font.BOLD, 16));
//        numOfQuestionsTextField.setBounds(200, 190, 148, 26);
//        numOfQuestionsTextField.setForeground(CommonConstants.BG);
//        numOfQuestionsTextField.setBackground(CommonConstants.BLACK);
//        add(numOfQuestionsTextField);




        addAnswerComponents(modernFont);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(modernFont);
        submitButton.setBounds(300,450, 262,45);
        submitButton.setBackground(new Color(0xFFA500));
        submitButton.setForeground(Color.BLACK);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    String question = questionTextArea.getText();
                    String category = categoryTextField.getText();
                    String[] answers = new String[answerTextFields.length];
                    int correctIndex = 0;
                    for (int i = 0; i < answerTextFields.length; i++) {
                        answers[i] = answerTextFields[i].getText();
                        if (answerRadioButtons[i].isSelected()) {
                            correctIndex = i;
                        }
                    }

                    if (JDBC.saveQuestionCategoryAndAnswersToDatabase(question, category, answers, correctIndex)) {
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this, "Successfully Added Question!");
                        resetFields();
                    } else {
                        JOptionPane.showMessageDialog(CreateQuestionScreenGui.this, "Failed To Add Question...");
                    }
                } else {
                    JOptionPane.showMessageDialog(CreateQuestionScreenGui.this, "Error: Invalid Input");
                }
            }
        });
        add(submitButton);

        JLabel goBackLabel = new JLabel("Go Back");
        goBackLabel.setFont(modernFont);
        goBackLabel.setBounds(300,500, 262,20);
        goBackLabel.setForeground(new Color(0xFFA500));
        goBackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        goBackLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                originalTitleScreen.dispose();
                TitleScreenGui titleScreenGui = new TitleScreenGui();
                titleScreenGui.setLocationRelativeTo(CreateQuestionScreenGui.this);
                CreateQuestionScreenGui.this.dispose();
                titleScreenGui.setVisible(true);
            }
        });
        add(goBackLabel);
    }

    private void addAnswerComponents(Font modernFont) {
        int verticalSpacing = 100;
        for (int i = 0; i < 4; i++) {
            JLabel answerLabel = new JLabel("Answer #" + (i+1));
            answerLabel.setFont(modernFont);
            answerLabel.setBounds(470,60 + (i * verticalSpacing),93,20);
            answerLabel.setForeground(new Color(0xFFA500));
            add(answerLabel);

            answerRadioButtons[i] = new JRadioButton();
            answerRadioButtons[i].setBounds(440, 100 + (i * verticalSpacing), 21, 21);
            answerRadioButtons[i].setBackground(new Color(30, 30, 30));
            buttonGroup.add(answerRadioButtons[i]);
            add(answerRadioButtons[i]);

            answerTextFields[i] = new JTextField();
            answerTextFields[i].setBounds(470, 90 + (i * verticalSpacing), 310, 36);
            answerTextFields[i].setFont(modernFont);
            answerTextFields[i].setForeground(Color.WHITE);
            answerTextFields[i].setBackground(new Color(50, 50, 50));
            add(answerTextFields[i]);
        }
        answerRadioButtons[0].setSelected(true);
    }

    private boolean validateInput() {
        if (questionTextArea.getText().replaceAll(" ", "").length() <= 0) return false;
        if (categoryTextField.getText().replaceAll(" ", "").length() <= 0) return false;
        for (int i = 0; i < answerTextFields.length; i++) {
            if (answerTextFields[i].getText().replaceAll(" ", "").length() <= 0)
                return false;
        }
        return true;
    }

    private void resetFields() {
        questionTextArea.setText("");
        categoryTextField.setText("");
        for (int i = 0; i < answerTextFields.length; i++) {
            answerTextFields[i].setText("");
        }
    }
}