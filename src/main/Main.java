package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();

        //primaryStage.setTitle();
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
//        try {
//            Connection connection = DriverManager.getConnection("jdbc:sqlite:database/database.db");
//            Statement stmt = connection.createStatement();
//            stmt.setQueryTimeout(30);
//
//            System.out.println("db connected");
//        }
//
//        catch (SQLException e){
//            System.err.println(e.getMessage());
//            return;
//        }
        launch(args);
    }
}
