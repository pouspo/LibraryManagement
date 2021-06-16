package scenes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.AdminBook;
import models.AdminUser;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AdminPage implements Initializable {
    public ToggleButton btnBooks;
    public ToggleButton btnUsers;
    public Pane booksPane;
    public Pane userPane;
    public TableView<AdminBook> table_book;
    public TableColumn<AdminBook, String> col_author;
    public TableColumn<AdminBook, String> col_bookname;
    public TableColumn<AdminBook, String> col_published;
    public TableColumn<AdminBook, Integer> col_quantity;

    public TableColumn<AdminUser, String> col_username;
    public TableColumn<AdminUser, String> col_email;
    public TableColumn<AdminUser, String> col_password;
    public TableColumn<AdminUser, String> col_isSuperUser;
    public TableView<AdminUser> table_user;
    public TableColumn col_user_action;
    public TableColumn col_book_action;
    public Pane edit_bookPane;
    public Button save_book_btn;
    public Button cancel_book_btn;
    public TextField book_author;
    public TextField book_name;
    public TextField book_quantity;
    public TextField book_publishedOn;
    public Pane edit_user_pane;
    public TextField user_name;
    public TextField user_email;
    public TextField user_password;
    public Button userPaneSaveButton;
    public Button userPaneCancelButton;
    public RadioButton user_isSuperUser;
    public Button logOutButton;
    public AnchorPane adminPagePane;

    ObservableList<AdminBook> adminBookObservableList = FXCollections.observableArrayList();
    ObservableList<AdminUser> adminUserObservableList = FXCollections.observableArrayList();
    int selectedBookId;
    String selectedUserEmail;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        col_author.setCellValueFactory(new PropertyValueFactory<>("Author"));
        col_bookname.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        col_published.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        Callback<TableColumn<AdminBook, String>, TableCell<AdminBook, String>> cellBookFactory = (params) -> {
            final TableCell<AdminBook, String> cell = new TableCell<AdminBook, String>(){
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    if(b){
                        setGraphic(null);
                        setText(null);
                    }else {
                        final Button editButton = new Button("Edit");
                        editButton.setOnAction(actionEvent -> {
                            AdminBook book = getTableView().getItems().get(getIndex());
                            selectedBookId = book.getBookId();
                            book_author.setText(book.getAuthor());
                            book_name.setText(book.getBookName());
                            book_publishedOn.setText(book.getBookName());
                            book_quantity.setText(String.valueOf(book.getQuantity()));
                            //System.out.println(book.getBookId());
                            edit_bookPane.toFront();

                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        col_book_action.setCellFactory(cellBookFactory);
        populateBooklist();
        table_book.setItems(adminBookObservableList);

        col_username.setCellValueFactory(new PropertyValueFactory<>("userName"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_password.setCellValueFactory(new PropertyValueFactory<>("password"));
        col_isSuperUser.setCellValueFactory(new PropertyValueFactory<>("superuser"));

        Callback<TableColumn<AdminUser, String>, TableCell<AdminUser, String>> cellUserFactory = (params) -> {
            final TableCell<AdminUser, String> cell = new TableCell<AdminUser, String>(){
                @Override
                protected void updateItem(String s, boolean b) {
                    super.updateItem(s, b);
                    if(b){
                        setGraphic(null);
                        setText(null);
                    }else {
                        final Button editButton = new Button("Edit");
                        editButton.setOnAction(actionEvent -> {
                            AdminUser user = getTableView().getItems().get(getIndex());
                            edit_user_pane.toFront();
                            user_name.setText(user.getUserName());

                            user_email.setText(user.getEmail());
                            selectedUserEmail = user.getEmail();

                            user_email.setEditable(false);
                            user_password.setText(user.getPassword());

                            if(user.getSuperuser().equals("true")) user_isSuperUser.setSelected(true);
                            else user_isSuperUser.setSelected(false);
                        });
                        setGraphic(editButton);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        col_user_action.setCellFactory(cellUserFactory);

        populateUserlist();
        table_user.setItems(adminUserObservableList);
    }

    public void logOutButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) adminPagePane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/main.fxml"));
        Parent root =  loader.load();
        //primaryStage.setTitle();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void populateBooklist(){
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn!= null)
            {
                String query = "Select * from Book_Information";
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while(res.next()){
                    adminBookObservableList.add(new AdminBook(
                            res.getInt("ID"),
                            res.getInt("quantity"),
                            res.getString("name"),
                            res.getString("author"),
                            res.getString("published_on")
                    ));

                }

            }
            conn.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

    }
    public void populateUserlist(){
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn!= null)
            {
                String query = "Select * from user_table";
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);
                while(res.next()){
                    adminUserObservableList.add(new AdminUser(
                            res.getString("username"),
                            res.getString("email"),
                            res.getString("password"),
                            res.getString("isSuperUser")

                    ));

                }

            }
            conn.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

    }

    public void toggleBtnPressed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnBooks)
        {
            booksPane.toFront();
        }
        else
            userPane.toFront();
    }

    public void cancelBookBtnAction(ActionEvent actionEvent) {
        adminBookObservableList.clear();
        populateBooklist();
        table_book.setItems(adminBookObservableList);
        booksPane.toFront();
    }

    public void save_book_button_action(ActionEvent actionEvent) {
        System.out.println(selectedBookId);
        String bookName = book_name.getText();
        String author = book_author.getText();
        String publishedDate = book_publishedOn.getText();
        Integer quantity = Integer.parseInt(book_quantity.getText());
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn!= null)
            {
                String query = String.format("UPDATE Book_Information\n" +
                        "  SET name = '%s', author = '%s', published_on = '%s', quantity = %d\n" +
                        "  WHERE ID = %d",bookName,author,publishedDate,quantity, selectedBookId);
                Statement stmt = conn.createStatement();
                stmt.execute(query);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setContentText("Book information updated successfully..");
                alert.showAndWait();
            }
            conn.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Database update error!");
            alert.showAndWait();
        }

    }

    public void userPaneSaveButtonAction(ActionEvent actionEvent) {
        String username = user_name.getText();
        String password = user_password.getText();
        boolean isSuper = user_isSuperUser.isSelected();
        String query;
        if(isSuper) query = String.format("UPDATE user_table SET username='%s', password='%s', isSuperUser='true' WHERE email='%s'", username, password, selectedUserEmail);
        else query = String.format("UPDATE user_table SET username='%s', password='%s', isSuperUser='false' WHERE email='%s'", username, password, selectedUserEmail);
        try{
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn!= null)
            {
                Statement stmt = conn.createStatement();
                stmt.execute(query);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success!");
                alert.setContentText("User information updated successfully..");
                alert.showAndWait();
            }
            conn.close();
        }
        catch (SQLException throwables){
            System.out.println(throwables);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setContentText("Database update error!");
            alert.showAndWait();
        }
    }

    public void userPaneCancelButtonAction(ActionEvent actionEvent) {
        adminUserObservableList.clear();
        populateUserlist();
        table_user.setItems(adminUserObservableList);
        userPane.toFront();
    }
}
