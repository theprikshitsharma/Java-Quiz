package database;

import java.sql.*;
import java.sql.DriverManager;
import java.util.ArrayList;

public class JDBC {
    // MySQL config
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz_gui_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Edusecure@123";


    public static boolean saveQuestionCategoryAndAnswersToDatabase(String question, String category, String[] answers, int correctIndex) {
     try {
        // establish a database connection
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // insert category if it's new, otherwise retrive it from the database
        Category categoryObj = getCategory(category);
        if(categoryObj == null) {
            // insert new category to database
            categoryObj = insertCategory(category);
        }

        Question questionObj = insertQuestion(categoryObj, question);
         return insertAnswers(questionObj, answers, correctIndex);
     } 
     
     catch (Exception e) {
        e.printStackTrace();
        }   

        return false;
    }

    // question methods
    private static Question insertQuestion(Category category , String questionText) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    
            PreparedStatement insertQuestionQuery = connection.prepareStatement("INSERT INTO QUESTION (CATEGORY_ID, QUESTION_TEXT) " + "VALUES (? , ?)", Statement.RETURN_GENERATED_KEYS);
            insertQuestionQuery.setInt(1, category.getCategoryId());
            insertQuestionQuery.setString(2, questionText);
            insertQuestionQuery.executeUpdate();

            // check for the question id
            ResultSet resultSet = insertQuestionQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int questionId = resultSet.getInt(1);
                return new Question(questionId, category.getCategoryId(), questionText);
            }

          } catch (Exception e) {
            e.printStackTrace();
          }

          // returns null if there was an error
          return null;
    }
    // category methods 
    public static Category getCategory(String category) {
      try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        PreparedStatement getCategoryQuery = connection.prepareStatement("SELECT * FROM CATEGORY WHERE CATEGORY_NAME = ?");
        getCategoryQuery.setString(1, category);

        ResultSet resultSet = getCategoryQuery.executeQuery();
        if (resultSet.next()) {
            // found the category
            int categoryId = resultSet.getInt("category_id");
            return new Category(categoryId, category);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }

      // returns null if it could not find the category in the database
      return null;

    }
    private static Category insertCategory(String category) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    
            PreparedStatement insertCategoryQuery = connection.prepareStatement("INSERT INTO CATEGORY(CATEGORY_NAME) " + "VALUES(?)" , Statement.RETURN_GENERATED_KEYS);
            insertCategoryQuery.setString(1, category);
            insertCategoryQuery.executeUpdate();

            ResultSet resultSet = insertCategoryQuery.getGeneratedKeys();
            if (resultSet.next()) {
                int categoryId = resultSet.getInt(1);
                return new Category(categoryId, category);
            }

    
          } catch (Exception e) {
            e.printStackTrace();
          }
    
          // returns null if it could not find the category in the database
          return null;
    
    }
    public static ArrayList<String> getCategories() {
      ArrayList<String> categoryList = new ArrayList<>();
      try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        Statement getCategoriesQuery = connection.createStatement();
        ResultSet resultSet = getCategoriesQuery.executeQuery("SELECT * FROM CATEGORY");

        while (resultSet.next()) {
          
          String categoryName = resultSet.getString("category_name");
          categoryList.add(categoryName);

        }
        return categoryList;

      } catch (Exception e) {
        e.printStackTrace();
      }

      // returns null if it could not find the categoies in the database
      return null;
    }
    
    // answer methods 
    public static ArrayList<Question> getQuestions(Category category) {
      ArrayList<Question> questions = new ArrayList<>();
      try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // query that retrieves all the questions of a category in random order
        PreparedStatement getQuestionsQuery = connection.prepareStatement("SELECT * FROM QUESTION JOIN CATEGORY " + "ON QUESTION.CATEGORY_ID = CATEGORY.CATEGORY_ID " + "WHERE CATEGORY.CATEGORY_NAME = ? ORDER BY RAND()");
        getQuestionsQuery.setString(1, category.getCategoryName());

        ResultSet resultSet = getQuestionsQuery.executeQuery();
        while (resultSet.next()) {
          int questionId = resultSet.getInt("question_id");
          int categoryId = resultSet.getInt("category_id");
          String question = resultSet.getString("question_text");
          questions.add(new Question(questionId, categoryId, question));
        }


        return questions;
      } 
      catch (Exception e) {
        e.printStackTrace();
      }

      // returns null if it could not find the categoies in the database
      return null;
    }

    public static ArrayList<Answer> getAnswers(Question question) {
      ArrayList<Answer> answers = new ArrayList<>();
      try {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

        // query that retrieves all the questions of a category in random order
        PreparedStatement getAnswersQuery = connection.prepareStatement("SELECT * FROM QUESTION JOIN ANSWER " + "ON QUESTION.QUESTION_ID = ANSWER.QUESTION_ID " + "WHERE QUESTION.QUESTION_ID = ? ORDER BY RAND()");
        getAnswersQuery.setInt(1, question.getQuestionId());
        ResultSet resultSet = getAnswersQuery.executeQuery();
        while (resultSet.next()) {
          int answerId = resultSet.getInt("answer_id");
          String answerText = resultSet.getString("answer_text");
          boolean isCorrect = resultSet.getBoolean("is_correct");
          Answer answer = new Answer(answerId, question.getQuestionId(), answerText, isCorrect);
          answers.add(answer);
        }

        return answers;
      } 
      catch (Exception e) {
        e.printStackTrace();
      }

      // returns null if it could not find the categoies in the database
      return null;
    }

    // true - successfully inserted answers
    // false - failden to insert answers
    private static boolean insertAnswers(Question question, String[] answers, int correctIndex) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    
            PreparedStatement insertAnswersQuery = connection.prepareStatement("INSERT INTO ANSWER (QUESTION_ID, ANSWER_TEXT, IS_CORRECT) " + "VALUES (? , ?, ?)");
            insertAnswersQuery.setInt(1, question.getQuestionId());

            for (int i = 0; i < answers.length; i++) {
                insertAnswersQuery.setString(2, answers[i]);
                if (i == correctIndex) {
                    insertAnswersQuery.setBoolean(3, true);
                }
                else {
                    insertAnswersQuery.setBoolean(3, false);
                }

                insertAnswersQuery.executeUpdate();

            }

            return true;

          } catch (Exception e) {
            e.printStackTrace();
          }

          return false;

    }
}
