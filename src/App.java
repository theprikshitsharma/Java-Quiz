import javax.swing.*;

import database.Category;
import screens.CreateQuestionScreenGui;
import screens.QuizScreenGui;
import screens.LoginSignupScreen;
import screens.TitleScreenGui;

public class App {
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            new LoginSignupScreen().setVisible(true);
        });
    }
}

