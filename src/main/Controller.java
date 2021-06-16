package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scenes.UserPage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public Label welcomeLabel;
    public Label UserEmailLabel;
    public Label PasswordLabel;
    public TextField PasswordText;
    public TextField UserEmailText;
    public Button SignInButton;
    public Button SignUpButton;
    public Label text;
    public AnchorPane Pane1;
    public RadioButton asAdminButton;

    public void SignInButtonAction(ActionEvent actionEvent)throws Exception {
        String email;
        String pass;

        email = UserEmailText.getText();
        pass = PasswordText.getText();


        Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
        if(asAdminButton.isSelected()){
            if(conn != null)
            {
                try{
                    Statement stmt = conn.createStatement();
                    String query = String.format("SELECT count(email) as cnt FROM user_table WHERE email='%s' AND password='%s' AND isSuperUser='true'", email, pass);
                    ResultSet res = stmt.executeQuery(query);
                    int cnt = res.getInt("cnt");
                    if(cnt == 1)
                    {
                        Stage stage = (Stage) Pane1.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/AdminPage.fxml"));
                        Parent root =  loader.load();
                        //primaryStage.setTitle();
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                    else
                    {
                        text.setText("email or password incorrect");
                    }
                } catch (SQLException e){
                    System.err.println(e.getMessage());
                    text.setText(e.getMessage());
                }
            }
            else{
                text.setText("Database connection error");
            }
        }
        else {
            if(conn != null){
                try {
                    Statement stmt = conn.createStatement();
                    String query = String.format("SELECT count(email) as cnt FROM user_table WHERE email='%s' AND password='%s' AND isSuperUser='false'", email, pass);
                    ResultSet res = stmt.executeQuery(query);

                    int cnt = res.getInt("cnt");

                    if(cnt == 1){
                        Stage stage = (Stage) Pane1.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene1/UserPage.fxml"));
                        Parent root =  loader.load();
                        UserPage userPage =  loader.getController();
                        userPage.setEmail(email);
                        //primaryStage.setTitle();
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                    else {
                        text.setText("email or password incorrect");
                    }
                } catch (SQLException e){
                    text.setText(e.getMessage());
                }
            }
            else
                text.setText("DB connection error");
        }



    }

    public void SignUpButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) Pane1.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/scenes/SignUp.fxml"));
        //primaryStage.setTitle();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
