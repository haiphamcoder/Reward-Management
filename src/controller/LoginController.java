/**
 * @author - Pham Ngoc Hai
 */
package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    // attributes and components
    private double x, y;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtUsername;

    /**
     * method login() used to login to main dashboard
     */
    @FXML
    void login(ActionEvent event) throws SQLException, IOException {
        if (validate()) {
            if (Database.checkLogin(txtUsername.getText(), txtPassword.getText())) {
                ((Node) event.getSource()).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainUI.fxml")));
                Scene scene = new Scene(root, 1000, 600);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();
            }
        }
    }

    /**
     * method validate() used to check the input data of username and password
     * @return true if input data is not empty, false otherwise
     */
    private boolean validate() {
        String userName = txtUsername.getText().trim();
        if(userName.isEmpty()){
            Message.showErrorMessage("Username is empty!");
            return false;
        }
        String passWord = txtPassword.getText().trim();
        if(passWord.isEmpty()){
            Message.showErrorMessage("Password is empty!");
            return false;
        }
        return true;
    }

    /**
     * method getXY() used to get x,y coordinates of the mouse when it press
     */
    @FXML
    void getXY(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            x = event.getSceneX();
            y = event.getSceneY();
        }
    }

    /**
     * methods updateLocation() used to update location of the window when drag mouse
     */
    @FXML
    void updateLocation(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        }
    }

    /**
     * method close() used to close the window
     */
    @FXML
    void close() {
        Platform.exit();
    }
}
