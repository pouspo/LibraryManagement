package scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp {
    public Label labelName;
    public Label labelEmail;
    public Label labelPass;
    public TextField TextName;
    public TextField TextEmail;
    public TextField TextPass;
    public Button signUpButton;
    public Button backButton;
    public Label WelcomeSignUP;
    public AnchorPane PanePage2;
    public Text errorText;

    public void sIgnUpButtonAction(ActionEvent actionEvent) throws Exception {
        String username = TextName.getText();
        String email = TextEmail.getText();
        String password = TextPass.getText();

        if(username.length() < 5 || username.length() > 20) {
            errorText.setText("Username should be of length 5-20");
            return;
        }

        if(email.length() < 5 || email.length() > 20){
            errorText.setText("Email should be of length 5-20");
            return;
        }

        if(password.length() < 5 || password.length() > 20){
            errorText.setText("Password should be of length 5-20");
            return;
        }

        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");

        if(conn != null){
            String sql = String.format("INSERT INTO user_table (username, email, password) VALUES('%s','%s','%s')",username, email, password);

            try{
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                System.out.println("Successfully created user");
                errorText.setText("Successfully created user");
                conn.close();
            } catch (SQLException e){
                System.out.println(e.getMessage());
                conn.close();
            }
        }

    }

    public void BackButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) PanePage2.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/main/main.fxml"));
        //primaryStage.setTitle();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
