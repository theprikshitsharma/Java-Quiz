package screens;

import constants.CommonConstants;
import database.Answer;
import database.Category;
import database.JDBC;
import database.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizScreenGui extends JFrame implements ActionListener {

    private JLabel scoreLabel;
    private JTextArea questionTextArea;
    private JButton[] answerButtons;
    private JButton nexButton;

    private Category category;
    private ArrayList<Question> questions;
    private Question currentQuestion;
    private int currentQuestionNumber;
    private int numOfQuestions;
    private int score;
    private boolean firstChoiceMade;

    public QuizScreenGui(Category category, int numOfQuestions) {
        super("Quiz");
        setSize(400, 565);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(CommonConstants.BG);

        answerButtons = new JButton[4];
        this.category = category;
        questions = JDBC.getQuestions(category);
        this.numOfQuestions = Math.min(numOfQuestions, questions.size());

        for (Question question : questions) {
            ArrayList<Answer> answers = JDBC.getAnswers(question);
            question.setAnswers(answers);
        }

        currentQuestion = questions.get(currentQuestionNumber);
        addGuiComponents();
    }

    private void addGuiComponents() {
        JLabel topicLabel = new JLabel("Topic: " + category.getCategoryName());
        topicLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topicLabel.setBounds(15, 15, 250, 20);
        topicLabel.setForeground(CommonConstants.BLACK);
        add(topicLabel);

        scoreLabel = new JLabel("Score: " + score + "/" + numOfQuestions);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scoreLabel.setBounds(270, 15, 96, 20);
        scoreLabel.setForeground(CommonConstants.BLACK);
        add(scoreLabel);

        questionTextArea = new JTextArea(currentQuestion.getQuestionText());
        questionTextArea.setFont(new Font("Segoe UI", Font.BOLD, 22));
        questionTextArea.setBounds(15, 50, 350, 91);
        questionTextArea.setLineWrap(true);
        questionTextArea.setWrapStyleWord(true);
        questionTextArea.setEditable(false);
        questionTextArea.setForeground(CommonConstants.BG);
        questionTextArea.setBackground(CommonConstants.BLACK);
        questionTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(questionTextArea);

        addAnswerComponents();

        JButton returnToTitleButton = new JButton("Return to Title");
        returnToTitleButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        returnToTitleButton.setBounds(60, 420, 262, 35);
        returnToTitleButton.setForeground(CommonConstants.BG);
        returnToTitleButton.setBackground(CommonConstants.BLACK);
        returnToTitleButton.setFocusPainted(false);
        returnToTitleButton.setBorderPainted(false);
        returnToTitleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnToTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TitleScreenGui titleScreenGui = new TitleScreenGui();
                titleScreenGui.setLocationRelativeTo(QuizScreenGui.this);
                QuizScreenGui.this.dispose();
                titleScreenGui.setVisible(true);
            }
        });
        add(returnToTitleButton);

        nexButton = new JButton("Next");
        nexButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nexButton.setBounds(240, 470, 80, 35);
        nexButton.setForeground(CommonConstants.BG);
        nexButton.setBackground(CommonConstants.BLACK);
        nexButton.setVisible(false);
        nexButton.setFocusPainted(false);
        nexButton.setBorderPainted(false);
        nexButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nexButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                nexButton.setVisible(false);
                firstChoiceMade = false;
                currentQuestion = questions.get(++currentQuestionNumber);
                questionTextArea.setText(currentQuestion.getQuestionText());
                for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
                    Answer answer = currentQuestion.getAnswers().get(i);
                    answerButtons[i].setBackground(Color.WHITE);
                    answerButtons[i].setText(answer.getAnswerText());
                }
            }
        });
        add(nexButton);
    }

    private void addAnswerComponents() {
        int verticalSpacing = 60;
        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            Answer answer = currentQuestion.getAnswers().get(i);
            JButton answerButton = new JButton(answer.getAnswerText());
            answerButton.setBounds(60, 180 + (i * verticalSpacing), 262, 45);
            answerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            answerButton.setHorizontalAlignment(SwingConstants.LEFT);
            answerButton.setBackground(Color.WHITE);
            answerButton.setForeground(CommonConstants.DARK_BLUE);
            answerButton.setFocusPainted(false);
            answerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            answerButton.addActionListener(this);
            answerButtons[i] = answerButton;
            add(answerButtons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton answerButton = (JButton) e.getSource();
        Answer correctAnswer = null;
        for (Answer answer : currentQuestion.getAnswers()) {
            if (answer.isCorrect()) {
                correctAnswer = answer;
                break;
            }
        }

        if (answerButton.getText().equals(correctAnswer.getAnswerText())) {
            answerButton.setBackground(CommonConstants.LIGHT_GREEN);
            if (!firstChoiceMade) {
                scoreLabel.setText("Score: " + (++score) + "/" + numOfQuestions);
            }
            if (currentQuestionNumber == numOfQuestions - 1) {
                JOptionPane.showMessageDialog(QuizScreenGui.this, "You're final score is " + score + "/" + numOfQuestions);
            } else {
                nexButton.setVisible(true);
            }
        } else {
            answerButton.setBackground(CommonConstants.LIGHT_RED);
        }
        firstChoiceMade = true;
    }
}
