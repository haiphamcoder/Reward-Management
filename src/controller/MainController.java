/**
 * @author - Pham Ngoc Hai
 */
package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    // attributes and conponents
    @FXML
    private TextField txtSearch;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button btnUpdate;
    @FXML
    private HBox hbResearchPapers;
    @FXML
    private HBox hbServiceHours;
    @FXML
    private HBox hbTeachingHours;
    @FXML
    private ImageView imageStaff;
    @FXML
    private MenuBar menuBar;
    @FXML
    private RadioButton rdbBirthday;
    @FXML
    private RadioButton rdbFemale;
    @FXML
    private RadioButton rdbID;
    @FXML
    private RadioButton rdbMale;
    @FXML
    private RadioButton rdbName;
    @FXML
    private RadioButton rdbResearch;
    @FXML
    private RadioButton rdbService;
    @FXML
    private RadioButton rdbTeaching;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhone;
    @FXML
    private TextField txtResearchPapers;
    @FXML
    private TextField txtServiceHours;
    @FXML
    private TextField txtTeachingHours;
    @FXML
    private TableColumn<Staff, LocalDate> tbcBirthday;
    @FXML
    private TableColumn<Staff, String> tbcID;
    @FXML
    private TableColumn<Staff, String> tbcName;
    @FXML
    private TableColumn<Staff, String> tbcType;
    @FXML
    private TableView<Staff> tbvStaff;
    private ObservableList<Staff> listStaff;
    private int selectedIndexStaff = -1;

    /**
     * method changePassword() used to show the change administrator password dialog
     */
    @FXML
    void changePassword() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/ChangePasswordUI.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Change administrator password");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * method logout() used to logout admin account from main dashboard and show login window again
     */
    @FXML
    void logout() throws IOException {
        menuBar.getScene().getWindow().hide();
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/LoginUI.fxml")));
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * method addStaff() used to request add new staff to the database
     */
    @FXML
    void addStaff() {
        if (validate()) {
            Staff staff;
            if (rdbTeaching.isSelected()) {
                staff = new TeachingStaff();
                ((TeachingStaff) staff).setTeachingHours(Double.parseDouble(txtTeachingHours.getText()));
            } else if (rdbResearch.isSelected()) {
                staff = new ResearchStaff();
                ((ResearchStaff) staff).setResearchPapers(Integer.parseInt(txtResearchPapers.getText()));
            } else {
                staff = new ServiceStaff();
                ((ServiceStaff) staff).setServiceHours(Double.parseDouble(txtServiceHours.getText()));
            }
            staff.setId(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            staff.setName(txtName.getText());
            staff.setDob(LocalDate.parse(datePicker.getEditor().getText(), DateTimeFormatter.ofPattern("dd/MM" +
                    "/yyyy")));
            staff.setGender(rdbMale.isSelected() ? Gender.Male : Gender.Female);
            staff.setPhone(txtPhone.getText());
            if (Database.addStaff(staff, imageStaff.getImage().getUrl().substring(6))) {
                showSearchedListStaff();
                selectedIndexStaff = listStaff.size() - 1;
                tbvStaff.getSelectionModel().select(selectedIndexStaff);
                Message.showInformationMessage("Add new staff successful!");
            } else {
                Message.showErrorMessage("Add new staff failed");
            }
        }
    }

    /**
     * method clearDetailStaff() used to clear the detail information of staff in Detail area
     */
    @FXML
    void clearDetailStaff() {
        txtName.clear();
        txtPhone.clear();
        datePicker.getEditor().clear();
        txtTeachingHours.clear();
        txtResearchPapers.clear();
        txtServiceHours.clear();
        imageStaff.setImage(null);
        tbvStaff.getSelectionModel().clearSelection();
        btnUpdate.setDisable(tbvStaff.getSelectionModel().getSelectedIndex() == -1);
    }

    /**
     * method checkSelectedType() used to set states of these components corresponding each type staff
     */
    @FXML
    void checkSelectedType() {
        if (rdbTeaching.isSelected()) {
            hbTeachingHours.setDisable(false);
            hbResearchPapers.setDisable(true);
            hbServiceHours.setDisable(true);
        } else if (rdbResearch.isSelected()) {
            hbTeachingHours.setDisable(true);
            hbResearchPapers.setDisable(false);
            hbServiceHours.setDisable(true);
        } else {
            hbTeachingHours.setDisable(true);
            hbResearchPapers.setDisable(true);
            hbServiceHours.setDisable(false);
        }
    }

    /**
     * method updateStaff() used to request update information of a exist staff
     */
    @FXML
    void updateStaff() {
        if (validate()) {
            Staff staff;
            if (rdbTeaching.isSelected()) {
                staff = new TeachingStaff();
                ((TeachingStaff) staff).setTeachingHours(Double.parseDouble(txtTeachingHours.getText()));
            } else if (rdbService.isSelected()) {
                staff = new ServiceStaff();
                ((ServiceStaff) staff).setServiceHours(Double.parseDouble(txtServiceHours.getText()));
            } else {
                staff = new ResearchStaff();
                ((ResearchStaff) staff).setResearchPapers(Integer.parseInt(txtResearchPapers.getText()));
            }
            staff.setId(tbvStaff.getSelectionModel().getSelectedItem().getId());
            staff.setName(txtName.getText());
            staff.setPhone(txtPhone.getText());
            staff.setGender(rdbMale.isSelected() ? Gender.Male : Gender.Female);
            staff.setDob(LocalDate.parse(datePicker.getEditor().getText(), DateTimeFormatter.ofPattern("dd/MM" +
                    "/yyyy")));
            if (Database.updateStaff(staff, imageStaff.getImage().getUrl().substring(6))) {
                showSearchedListStaff();
                tbvStaff.getSelectionModel().select(selectedIndexStaff);
                showStaffDetail();
                Message.showInformationMessage("Update staff successful!");
            } else {
                Message.showErrorMessage("Update staff failed!");
            }
        }
    }

    /**
     * method deleteStaff() used to request delete a staff in database
     */
    @FXML
    void deleteStaff() {
        selectedIndexStaff = tbvStaff.getSelectionModel().getSelectedIndex();
        if (selectedIndexStaff >= 0) {
            if (Message.showConfirmDialog("Delete this staff?")) {
                Staff staff = tbvStaff.getSelectionModel().getSelectedItem();
                if (Database.deleteStaff(staff.getId())) {
                    showSearchedListStaff();
                    selectedIndexStaff = 0;
                    tbvStaff.getSelectionModel().select(selectedIndexStaff);
                    showStaffDetail();
                } else {
                    Message.showErrorMessage("Delete staff failed!");
                }
            }
        } else {
            Message.showErrorMessage("No staff selected");
        }
    }

    /**
     * method updateStandard() used to show the update standard dialog
     */
    @FXML
    void updateStandard() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/UpdateStandardUI.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Update Standard");
        stage.setScene(scene);
        stage.showAndWait();
    }

    /**
     * method insertImage() used to choose and show image of staff in ImageView in Detail area
     */
    @FXML
    void insertImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Picture files", "*.png", "*.jpg"));
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            imageStaff.setImage(new Image(file.toURI().toString()));
        }
    }

    /**
     * method quit() used to exit program.
     */
    @FXML
    void quit() {
        Platform.exit();
    }

    /**
     * method validate() used to check the input data of information of staff in Detail area
     * @return true if the input data is correct, false otherwise
     */
    private boolean validate() {
        Image image = imageStaff.getImage();
        if (image == null) {
            Message.showErrorMessage("Image is empty!");
            return false;
        }
        if (txtName.getText().trim().isEmpty()) {
            Message.showErrorMessage("Name is empty!");
            return false;
        }
        String date = datePicker.getEditor().getText().trim();
        if (date.isEmpty()) {
            Message.showErrorMessage("Birthday is empty!");
            return false;
        } else {
            try {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                Message.showErrorMessage("Birthday format must be dd/MM/yyyy");
                return false;
            }
        }
        if (txtPhone.getText().trim().isEmpty()) {
            Message.showErrorMessage("Phone number is empty!");
            return false;
        } else if (!txtPhone.getText().trim().matches("^\\d{10}$")) {
            Message.showErrorMessage("Incorrect phone number!");
            return false;
        }
        if (rdbTeaching.isSelected()) {
            String teachingHours = txtTeachingHours.getText().trim();
            if (teachingHours.isEmpty()) {
                Message.showErrorMessage("Teaching hours is empty!");
                return false;
            } else if (!teachingHours.matches("[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?")) {
                Message.showErrorMessage("Teaching hours must be a floating point number!");
                return false;
            }
        } else if (rdbService.isSelected()) {
            String serviceHours = txtServiceHours.getText().trim();
            if (serviceHours.isEmpty()) {
                Message.showErrorMessage("Service hours is empty!");
                return false;
            } else if (!serviceHours.matches("[+-]?[0-9]+(\\.[0-9]+)?([Ee][+-]?[0-9]+)?")) {
                Message.showErrorMessage("Service hours must be a floating point number!");
                return false;
            }
        } else if (rdbResearch.isSelected()) {
            String researchPapers = txtResearchPapers.getText().trim();
            if (researchPapers.isEmpty()) {
                Message.showErrorMessage("Research papers is empty!");
                return false;
            } else if (!researchPapers.matches("[+-]?[0-9]+")) {
                Message.showErrorMessage("Research papers must be a integer!");
                return false;
            }
        }
        return true;
    }

    /**
     * method showStaffDetail() used to show the detail information of a selected staff in table
     */
    @FXML
    void showStaffDetail() {
        selectedIndexStaff = tbvStaff.getSelectionModel().getSelectedIndex();
        if (selectedIndexStaff >= 0) {
            btnUpdate.setDisable(false);
            Staff staff = tbvStaff.getSelectionModel().getSelectedItem();
            File file = Paths.get("/tmp/img/" + staff.getId()).toFile();
            imageStaff.setImage(new Image(file.toURI().toString()));
            txtName.setText(staff.getName());
            txtPhone.setText(staff.getPhone());
            datePicker.setValue(staff.getDob());
            if (staff.getGender().equals(Gender.Male)) {
                rdbMale.setSelected(true);
            } else {
                rdbFemale.setSelected(true);
            }
            if (staff.getType().equals(TypeStaff.TeachingStaff)) {
                rdbTeaching.setSelected(true);
                txtTeachingHours.setText(((TeachingStaff) staff).getTeachingHours() + "");
                txtServiceHours.clear();
                txtResearchPapers.clear();
            } else if (staff.getType().equals(TypeStaff.ServiceStaff)) {
                rdbService.setSelected(true);
                txtServiceHours.setText(((ServiceStaff) staff).getServiceHours() + "");
                txtTeachingHours.clear();
                txtResearchPapers.clear();
            } else {
                rdbResearch.setSelected(true);
                txtResearchPapers.setText(((ResearchStaff) staff).getResearchPapers() + "");
                txtTeachingHours.clear();
                txtServiceHours.clear();
            }
            checkSelectedType();
        } else {
            clearDetailStaff();
        }
    }

    /**
     * method showSearchedListStaff() used to show the list staff corresponding keyword in search box
     */
    @FXML
    void showSearchedListStaff() {
        String str = txtSearch.getText().trim();
        listStaff = Database.getAllStaffFromDb();
        ObservableList<Staff> result = FXCollections.observableArrayList();
        if (str.isEmpty()) {
            tbvStaff.setItems(listStaff);
        } else {
            if (rdbID.isSelected()) {
                for (Staff x : listStaff) {
                    if (x.getId().contains(str)) {
                        result.add(x);
                    }
                }
            } else if (rdbName.isSelected()) {
                for (Staff x : listStaff) {
                    if (x.getName().contains(str)) {
                        result.add(x);
                    }
                }
            } else if (rdbBirthday.isSelected()) {
                for (Staff x : listStaff) {
                    String strDate = x.getDob().toString();
                    if (strDate.contains(str)) {
                        result.add(x);
                    }
                }
            } else {
                for (Staff x : listStaff) {
                    if (x.getType().toString().contains(str)) {
                        result.add(x);
                    }
                }
            }
            tbvStaff.setItems(result);
        }

    }

    /**
     * method statisticReward() used to statistic all rewarded staff and show their in table
     */
    @FXML
    void statisticReward() {
        listStaff = Database.getAllStaffFromDb();
        ObservableList<Staff> result = FXCollections.observableArrayList();
        for (Staff x : listStaff) {
            if (x.isRewarded()) {
                result.add(x);
            }
        }
        tbvStaff.setItems(result);
        selectedIndexStaff = 0;
        tbvStaff.getSelectionModel().select(selectedIndexStaff);
        showStaffDetail();
    }

    /**
     * method get the list of staff has teachingHours greater than parameter
     * @param teachingHours a number of teaching hours you want to compare
     * @return a ObservableList<Staff> has teachingHours greater than parameter
     */
    private ObservableList<Staff> filtStaff(double teachingHours) {
        listStaff = Database.getAllStaffFromDb();
        ObservableList<Staff> result = FXCollections.observableArrayList();
        for (Staff x : listStaff) {
            if (x instanceof TeachingStaff) {
                if (((TeachingStaff) x).getTeachingHours() > teachingHours) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    /**
     * method get the list of staff has research papers less than parameter
     * @param researchPapers a number of research papers you want to compare
     * @return a ObservableList<Staff> has research papers less than parameter
     */
    private ObservableList<Staff> filtStaff(int researchPapers) {
        listStaff = Database.getAllStaffFromDb();
        ObservableList<Staff> result = FXCollections.observableArrayList();
        for (Staff x : listStaff) {
            if (x instanceof ResearchStaff) {
                if (((ResearchStaff) x).getResearchPapers() < researchPapers) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    /**
     * method get the list of staff has research papers less than parameter or teaching hours greater than parameter
     * @param researchPapers a number of research papers you want to compare
     * @param teachingHours a number of teaching hours you want to compare
     * @return a ObservableList<Staff> has research papers less than parameter or teaching hours greater than parameter
     */
    private ObservableList<Staff> filtStaff(double teachingHours, int researchPapers) {
        ObservableList<Staff> result = FXCollections.observableArrayList();
        for (Staff x : listStaff) {
            if (x instanceof TeachingStaff) {
                if (((TeachingStaff) x).getTeachingHours() > teachingHours) {
                    result.add(x);
                }
            } else if (x instanceof ResearchStaff) {
                if (((ResearchStaff) x).getResearchPapers() < researchPapers) {
                    result.add(x);
                }
            }
        }
        return result;
    }

    /**
     * method showFilterDialog() used to show the filted dialog
     */
    @FXML
    void showFilterDialog(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/FilterDialogUI.fxml")));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Filter Dialog");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.showAndWait();
        if (FilterData.filtResearch && !FilterData.filtTeaching) {
            listStaff = filtStaff(FilterData.researchPapers);
        } else if (!FilterData.filtResearch && FilterData.filtTeaching) {
            listStaff = filtStaff(FilterData.teachingHours);
        } else if (FilterData.filtResearch) {
            listStaff = filtStaff(FilterData.teachingHours, FilterData.researchPapers);
        }
        tbvStaff.setItems(listStaff);
        selectedIndexStaff = 0;
        tbvStaff.getSelectionModel().select(selectedIndexStaff);
        showStaffDetail();
    }

    @FXML
    void showAbout() {
        Message.showInformationMessage("Team 25\nVersion: v1.0.0");
    }

    /**
     * method showAllStaff() used to show all staff in table
     */
    @FXML
    void showAllStaff() {
        tbvStaff.setItems(Database.getAllStaffFromDb());
        selectedIndexStaff = 0;
        tbvStaff.getSelectionModel().select(selectedIndexStaff);
        showStaffDetail();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get list all staff in the datebase
        listStaff = Database.getAllStaffFromDb();
        Database.getStandard(); // set the standard indies for TeachingStaff, ServiceStaff and ResearchStaff

        // set default stata for components
        rdbTeaching.setSelected(true);
        rdbMale.setSelected(true);
        rdbID.setSelected(true);
        checkSelectedType();
        btnUpdate.setDisable(tbvStaff.getSelectionModel().getSelectedIndex() == -1);

        // set style for tableView and set property value for column in table
        tbvStaff.setStyle("-fx-selection-bar: #0096C9; -fx-selection-bar-non-focused: #0096C9");
        tbvStaff.setItems(listStaff);
        tbcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcBirthday.setCellValueFactory(new PropertyValueFactory<>("dob"));
        tbcType.setCellValueFactory(new PropertyValueFactory<>("type"));

        // add listener for search box when text change
        txtSearch.textProperty().addListener((observableValue, s, t1) -> showSearchedListStaff());
    }
}
