/**
 * @author - Pham Ngoc Hai
 */
package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Message {
    /**
     * method showErrorMessage() used to show error alert
     * @param message error message you want to show
     */
    public static void showErrorMessage(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * method showInformationMessage() used to show information alert
     * @param message message you want to show
     */
    public static void showInformationMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * method showConfirmMessage() used to show confirmation alert
     * @param message message you want to confirm
     */
    public static boolean showConfirmDialog(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
        return alert.getResult().equals(ButtonType.OK);
    }
}
