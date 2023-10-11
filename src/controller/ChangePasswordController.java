/**
 * @author - Nguyen Duy Thai
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;

import java.sql.SQLException;

public class ChangePasswordController {
    // main components
    @FXML
    private PasswordField txtNewPass;
    @FXML
    private PasswordField txtOldPass;
    @FXML
    private PasswordField txtRetypeNewPass;


    /**
     * method close() to close the change administrator dialog
     */
    @FXML
    void close(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * method confirm() used to confirm the password change
     */
    @FXML
    void confirm(ActionEvent event) throws SQLException {
        if (validate()) {
            if (Database.changeAdminPassword(txtOldPass.getText(), txtNewPass.getText())) {
                close(event);
                Message.showInformationMessage("Change administrator password successful!");
            }
        }
    }

    /**
     * method validate() used to validate the input data from ChangePasswordUI
     *
     * @return true if the input data is correct, false otherwise
     */
    private boolean validate() {
        if (!txtOldPass.getText().isEmpty() && !txtNewPass.getText().isEmpty() && !txtRetypeNewPass.getText().isEmpty()) {
            if (txtNewPass.getText().equals(txtRetypeNewPass.getText())) {
                return true;
            } else {
                Message.showErrorMessage("Re-enter new password does not match");
            }
        } else {
            Message.showErrorMessage("Information cannot be blank!");
        }
        return false;
    }
}
