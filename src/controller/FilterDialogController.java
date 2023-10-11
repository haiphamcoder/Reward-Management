/**
 * @author - Nguyen Duy Thai
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import model.FilterData;

import java.net.URL;
import java.util.ResourceBundle;

public class FilterDialogController implements Initializable {
    // main components
    @FXML
    private CheckBox ckbTeaching;
    @FXML
    private HBox hbTeachingHours;
    @FXML
    private TextField txtTeachingHours;
    @FXML
    private CheckBox ckbResearch;
    @FXML
    private HBox hbResearchPapers;
    @FXML
    private TextField txtResearchPapers;

    /**
     * method closeDialog() used to close the filter dialog
     */
    @FXML
    void closeDialog(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * method confirm() used to confirm the information of the option filter and the value of teaching hours and
     * research
     * papers you want to filt
     */
    @FXML
    void confirm(ActionEvent event) {
        if (validate()) {
            if (ckbTeaching.isSelected()) {
                FilterData.filtTeaching = true;
                FilterData.teachingHours = Double.parseDouble(txtTeachingHours.getText().trim());
            } else {
                FilterData.filtTeaching = false;
            }

            if (ckbResearch.isSelected()) {
                FilterData.filtResearch = true;
                FilterData.researchPapers = Integer.parseInt(txtResearchPapers.getText().trim());
            } else {
                FilterData.filtResearch = false;
            }
            closeDialog(event);
        }
    }

    /**
     * method validate() used to check information of input data
     * @return true if input date is correct, false otherwise
     */
    private boolean validate() {
        boolean result = true;
        if (ckbTeaching.isSelected()) {
            String teachingHours = txtTeachingHours.getText().trim().trim();
            if (teachingHours.isEmpty()) {
                Message.showErrorMessage("Number of teaching hours is empty!");
                result = false;
            } else if (!teachingHours.matches("[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?")) {
                Message.showErrorMessage("Number of teaching hours is incorrect format!");
                result = false;
            }
        }
        if (ckbResearch.isSelected()) {
            String researchPapers = txtResearchPapers.getText().trim();
            if (researchPapers.isEmpty()) {
                Message.showErrorMessage("Number of research papers is empty!");
                result = false;
            } else if (!researchPapers.matches("[+-]?[0-9]+")) {
                Message.showErrorMessage("Number of research papers is incorrect format!");
                result = false;
            }
        }
        return result;
    }

    /**
     * method checkSelectedOption() used to set the state of options
     */
    private void checkSelectedOption() {
        hbTeachingHours.setDisable(!ckbTeaching.isSelected());
        hbResearchPapers.setDisable(!ckbResearch.isSelected());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // set default state for checkbox teaching and research
        ckbTeaching.setSelected(true);
        ckbResearch.setSelected(true);

        // add change listener for checkbox
        ckbTeaching.selectedProperty().addListener((observableValue, aBoolean, t1) -> checkSelectedOption());
        ckbResearch.selectedProperty().addListener((observableValue, aBoolean, t1) -> checkSelectedOption());
    }
}
