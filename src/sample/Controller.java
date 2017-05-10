package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    TextField loginText;

    @FXML
    PasswordField passwordText;


    @FXML
    TextField loginTextReg;

    @FXML
    TextField lastNameReg;

    @FXML
    TextField telephoneReg;

    @FXML
    PasswordField passReg;

    @FXML
    PasswordField repeatedPassReg;

    private boolean isLoginFormValid() {
        if (loginText.getText().trim().length() < 3 || passwordText.getText().trim().length() < 4) {
            Utils.openDialog("Logowanie", "Login lub hasło zawierają za mało znaków. Login min 3 znaki, hasło min 4 znaki");
            return false;
        }
        return true;
    }

    public void openDialog(MouseEvent event) {

//        System.out.println("Login: " + loginText.getText());
//        System.out.println("Password: " + Utils.hashPassword(passwordText.getText()));

        if (!isLoginFormValid()) {
            return;
        }

        Statement statement = MySqlConnector.getInstance().getNewStatement();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE name = " + "'" + loginText.getText() + "' LIMIT 1");
            int counter = 0;
            while (resultSet.next()) {
                String passwordFromDatabase = resultSet.getString("password");
//                    if(passwordFromDatabase.equals(passwordText.getText())){

                if (passwordFromDatabase.equals(Utils.hashPassword(passwordText.getText()))) {


                    try {
                        Parent myPage = FXMLLoader.load(getClass().getResource("userView.fxml"));
                        Scene scene = new Scene(myPage);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        stage.hide();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Utils.openDialog("Logowanie", "Udało się zalogować!");
                } else {
                    Utils.openDialog("Logowanie", "Złe hasło!");
                }
                counter++;
            }
            if (counter == 0) {
                Utils.openDialog("Logowanie", "Nie ma takiego użytkownika w bazie, proszę utowrzyć użytkownika");
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(MouseEvent event) {

        if (passReg.getText().equals(repeatedPassReg.getText())) {

            try {
                Parent myPage = FXMLLoader.load(getClass().getResource("registrationCorrect.fxml"));
                Scene scene = new Scene(myPage);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.hide();
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

//            TODO: dodać walidację poprawnie wprowadzonego numeru telefonu

            String sgl = "INSERT INTO user (name, password, lastname, number) VALUES(?, ?, ?, ?)";
            try {
                PreparedStatement statement = MySqlConnector.getInstance().getConnection().prepareStatement(sgl);
                statement.setString(1, loginTextReg.getText());
                statement.setString(2, Utils.hashPassword(passReg.getText()));
                statement.setString(3, lastNameReg.getText());
                statement.setString(4, telephoneReg.getText());
                statement.execute();

//                Utils.openDialog("Logowanie", "Poprawnie założono konto");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            Utils.openDialog("Logowanie", "Hasła muszą być jednakowe!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
