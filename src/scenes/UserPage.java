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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Book;
import models.InfoTable;
import models.ModelTable;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class UserPage implements Initializable {
    public AnchorPane userPagePane;
    public TableView<ModelTable> table;
    public TableColumn<ModelTable, String> name;
    public TableColumn<ModelTable, String> author;
    public TableColumn<ModelTable, String> pub_date;
    //public Label setTextLabel;
    public Button searchBookButton;
    public ToggleButton bookListToggle;
    public ToggleButton informationToggle;
    public Pane bookListPane;
    public Pane informationPane;
    public TableColumn bookID;
    public ListView cartListView;

    public TableView<InfoTable> infoTable;
    public TableColumn infoBookID;
    public TableColumn<InfoTable, String> infoBookName;
    public TableColumn<InfoTable, String> infoBookDate;
    public TableColumn<InfoTable, Integer> infoFine;
    public Button logOutButton;

    String email;
    ObservableList<ModelTable> objlist = FXCollections.observableArrayList();
    ObservableList<Book> cartList = FXCollections.observableArrayList();
    ObservableList<InfoTable> infoTablesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        populateBookListTable();
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        pub_date.setCellValueFactory(new PropertyValueFactory<>("pub_date"));
        bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));

        infoBookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        infoBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        infoBookDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        infoFine.setCellValueFactory(new PropertyValueFactory<>("fine"));

        table.setItems(objlist);

        informationToggle.setSelected(true);
    }


    public void setEmail(String email){
        this.email = email;
        //this.setTextLabel.setText(email);
    }

    public void populateBookListTable() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn != null){
                String query = "SELECT * from Book_Information";
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);

                while(res.next()){
                    objlist.add(new ModelTable(
                            res.getString("name"),
                            res.getString("author"),
                            res.getString("published_on"),
                            res.getInt("ID")
                    ));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void populateInfoTable(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
            if(conn != null){
                String query = String.format("SELECT book_id, name, issue_date\n" +
                        " FROM booking\n" +
                        " INNER JOIN Book_Information\n" +
                        " on booking.book_id == Book_Information.ID\n" +
                        " WHERE booking.user_email='%s';", this.email);
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery(query);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                infoTablesList.clear();
                while(res.next()){
                    String issueDate = res.getString("issue_date");
                    Date dt = simpleDateFormat.parse(issueDate);

                    LocalDateTime borrowDate = LocalDateTime.parse(issueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    LocalDateTime currentDate = LocalDateTime.now();

                    long duration = Duration.between(borrowDate, currentDate).toDays();

                    long week = duration/7;

                    int fine = 0;

                    if (week > 1) {
                        fine = (int) week * 10;
                    }

                    infoTablesList.add(new InfoTable(
                            res.getInt("book_id"),
                            res.getString("name"),
                            simpleDateFormat.format(dt),
                            fine
                    ));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void toggleButtonAction(ActionEvent actionEvent) {
        if (actionEvent.getSource() == bookListToggle){
            bookListPane.toFront();
            informationToggle.setSelected(false);
        }
        if (actionEvent.getSource() == informationToggle) {
            populateInfoTable();
            infoTable.setItems(infoTablesList);

            informationPane.toFront();
            bookListToggle.setSelected(false);
        }
    }

    public void logOutButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) userPagePane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/sample.fxml"));
        Parent root =  loader.load();
        //primaryStage.setTitle();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void bookListRowSelected(ActionEvent actionEvent) {
        if(table.getSelectionModel().getSelectedItem() != null){

            int book_id = table.getSelectionModel().getSelectedItem().getBookID();
            String book_name = table.getSelectionModel().getSelectedItem().getName();
            Book book = new Book(book_id, book_name);
            if(!cartList.contains(book)){
                cartList.add(book);
                cartListView.setItems(cartList);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setContentText("This book is already in your CART!");
                alert.showAndWait();
            }
        }
    }
    public void backSpaceButtonAction(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.BACK_SPACE){
            if(cartListView.getSelectionModel().getSelectedItem() != null){
                cartList.remove(cartListView.getSelectionModel().getSelectedItem());
                cartListView.setItems(cartList);
            }
        }
    }

    public void checkoutCartAction(ActionEvent actionEvent) {
        if (cartList.size() > 0) {
            try {
                Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
                Statement statement = conn.createStatement();

                for (Book book : cartList) {
                    String query = String.format(
                            "SELECT count (user_email) as cnt FROM booking WHERE user_email='%s' AND book_id=%d", this.email, book.getBookID()
                    );

                    ResultSet res = statement.executeQuery(query);
                    int cnt = res.getInt("cnt");
                    if (cnt > 0) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error!");
                        alert.setContentText("You have already borrowed the book - [" + book.getBookName() + "]");
                        alert.showAndWait();
                        return;
                    }
                }

                for (Book book : cartList) {
                    String query = String.format(
                            "INSERT INTO booking (user_email, book_id) VALUES ('%s', %d)", this.email, book.getBookID()
                    );
                    statement.execute(query);
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText(String.format("Successfully Borrowd %d Books", cartList.size()));
                alert.showAndWait();

                cartList.clear();
                cartListView.setItems(cartList);
                conn.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setContentText("Query Execution Error");
                alert.showAndWait();
                System.out.println(throwables);
            }
        }
    }


    public void returnBookAction(ActionEvent actionEvent) {
        if(infoTable.getSelectionModel().getSelectedItem() != null){
            try{
                Connection conn = DriverManager.getConnection("jdbc:sqlite:database/database.db");
                if(conn != null){
                    String query = String.format("DELETE FROM booking WHERE user_email='%s' AND book_id=%d", this.email, infoTable.getSelectionModel().getSelectedItem().getBookId());
                    Statement statement = conn.createStatement();
                    statement.execute(query);
                    infoTablesList.remove(infoTable.getSelectionModel().getSelectedItem());
                    infoTable.setItems(infoTablesList);
                }

            }catch (SQLException e){
                e.printStackTrace();

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setContentText("Sql Exception occured");
                alert.showAndWait();
            }
        }
    }
}
