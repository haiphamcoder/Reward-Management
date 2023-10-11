/**
 * @author - Nguyen Duy Thai
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import model.ResearchStaff;
import model.ServiceStaff;
import model.TeachingStaff;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateStandardController implements Initializable {
    // attributes and components
    private double teachingHours;
    private double serviceHours;
    private int researchPapers;

    @FXML
    private TextField txtStandardResearch;
    @FXML
    private TextField txtStandardService;
    @FXML
    private TextField txtStandardTeaching;

    /**
     * method close() used to close the update standard dialog
     */
    @FXML
    void close(ActionEvent event) {
        ((Node) event.getSource()).getScene().getWindow().hide();
    }

    /**
     * method confirm() used to confirm update the standard indies of teaching hours, research papers and service hours
     */
    @FXML
    void confirm(ActionEvent event) throws SQLException {
        if (validate()) {
            close(event);
            if (Database.updateStandard(teachingHours, researchPapers, serviceHours)) {
                Message.showInformationMessage("Update successful!");
                TeachingStaff.setStandardTeachingHours(teachingHours);
                ServiceStaff.setStandardServiceHours(serviceHours);
                ResearchStaff.setStandardResearchPapers(researchPapers);
            } else {
                Message.showErrorMessage("Update failed!");
            }
        }
    }

    /**
     * method validate() used to check the input data of standard teaching hours, standard service hours and standard
     * research papers
     *
     * @return true if the input data is correct, false otherwise
     */
    private boolean validate() {
        String strTeachingHours = txtStandardTeaching.getText().trim();
        String strServiceHours = txtStandardService.getText().trim();
        String strResearchPapers = txtStandardResearch.getText().trim();
        if (!strTeachingHours.isEmpty() && !strServiceHours.isEmpty() && !strResearchPapers.isEmpty()) {
            try {
                teachingHours = Double.parseDouble(strTeachingHours);
                serviceHours = Double.parseDouble(strServiceHours);
                researchPapers = Integer.parseInt(strResearchPapers);
                return true;
            } catch (Exception e) {
                Message.showErrorMessage("The information entered is incorrect!");
            }
        } else {
            Message.showErrorMessage("The information cannot be blank!");
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtStandardResearch.setText((ResearchStaff.getStandardResearchPapers() + ""));
        txtStandardTeaching.setText(TeachingStaff.getStandardTeachingHours() + "");
        txtStandardService.setText(ServiceStaff.getStandardServiceHours() + "");
    }
}
