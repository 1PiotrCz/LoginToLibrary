package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

/**
 * Created by Piotr on 2017-05-10.
 */
public class UserViewController implements Initializable {

    @FXML
    ImageView logo;

    @FXML
    ListView list;

    @FXML
    TextField titleView;

    @FXML
    TextField authorView;

    @FXML
    TextField pageView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        list.setItems(loadBook());

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println(list.getSelectionModel().getSelectedItem());
            }
        });
    }

    private ObservableList<String> loadBook() {
        ObservableList<String> items = FXCollections.observableArrayList();
        Statement statement = MySqlConnector.getInstance().getNewStatement();

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM book");
            while (resultSet.next()) {
                items.add(resultSet.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private String[] loadBookData(String titleView) {
        Statement statement = MySqlConnector.getInstance().getNewStatement();
        String[] dataArray = new String[3];
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `books` WHERE `title`='" + titleView + "' LIMIT 1");
            while (resultSet.next()) {
                dataArray[0] = resultSet.getString("author");
                dataArray[1] = resultSet.getString("title");
                dataArray[2] = String.valueOf(resultSet.getInt("pages"));
                return dataArray;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}