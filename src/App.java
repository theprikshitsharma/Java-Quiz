import javax.swing.*;

import database.Category;
import screens.CreateQuestionScreenGui;
import screens.QuizScreenGui;
import screens.LoginSignupScreen;
import screens.TitleScreenGui;

public class App {
    public static void main(String[] args){
        System.out.println("Application Started.");
        SwingUtilities.invokeLater(() -> {
            new LoginSignupScreen().setVisible(true);
//            TitleScreenGui t = new TitleScreenGui();
//            new CreateQuestionScreenGui(t).setVisible(true);
        });
    }
}

