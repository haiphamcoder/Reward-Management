/**
 * @author - Pham Ngoc Hai
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;

public class Database {
    // attributes
    private static Connection connect;

    /**
     * method getConnection() used to establish a connection to the database
     *
     * @return a connection to the database
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/reward", "root", "");
            return connect;
        } catch (Exception e) {
            Message.showErrorMessage("Database connection failed!");
            return null;
        }
    }

    /**
     * method checkLogin() used to check the information of administator account
     *
     * @param username a username of administrator account you want to check
     * @param password a password of administrator account you want to check
     * @return true if the information of administrator is correct, false otherwise
     */
    public static boolean checkLogin(String username, String password) throws SQLException {
        boolean res = false;
        connect = getConnection();
        if (connect != null) {
            String sql = "SELECT * FROM admin_account WHERE Username=? and Password=AES_ENCRYPT(?,?)";
            PreparedStatement prepared = connect.prepareStatement(sql);
            prepared.setString(1, username);
            prepared.setString(2, password);
            prepared.setString(3, username);
            ResultSet resultSet = prepared.executeQuery();
            res = resultSet.next();
            if(!res){
                Message.showErrorMessage("Username or Password is incorrect!");
            }
            connect.close();
        }
        return res;
    }

    /**
     * method changeAdminPassword() used to perform password changes of administrator account in the database
     *
     * @param oldPass old password of administrator account
     * @param newPass new password of administrator account
     * @return true if change password successfully, false otherwise
     */
    public static boolean changeAdminPassword(String oldPass, String newPass) throws SQLException {
        boolean res = false;
        connect = getConnection();
        if (connect != null) {
            String sql = "SELECT * FROM admin_account WHERE Password=AES_ENCRYPT(?,'admin')";
            PreparedStatement prepared = connect.prepareStatement(sql);
            prepared.setString(1, oldPass);
            ResultSet result = prepared.executeQuery();
            if (result.next()) {
                String sqlUpdatePassword = "UPDATE admin_account SET Password=AES_ENCRYPT(?,'admin') WHERE " +
                        "Username='admin'";
                PreparedStatement preStatementUpdatePass = connect.prepareStatement(sqlUpdatePassword);
                preStatementUpdatePass.setString(1, newPass);
                int resultUpdate = preStatementUpdatePass.executeUpdate();
                res = resultUpdate != 0;
            } else {
                Message.showErrorMessage("Old password is incorrect!");
            }
            connect.close();
        }
        return res;
    }

    /**
     * method updateStandard() used to update information of standard index in the database
     *
     * @param teachingHours  a number of the standard teaching hours you want to update
     * @param researchPapers a number of the standard research papers you want to update
     * @param serviceHours   a number of the standard service hours you want to update
     * @return true if update information sucessfully, false other wise
     */
    public static boolean updateStandard(double teachingHours, int researchPapers, double serviceHours) throws SQLException {
        boolean res = false;
        connect = getConnection();
        if (connect != null) {
            String sql = "UPDATE standard SET TeachingHours=?, ResearchPapers=?, ServiceHours=? WHERE 1";
            PreparedStatement prepared = connect.prepareStatement(sql);
            prepared.setDouble(1, teachingHours);
            prepared.setInt(2, researchPapers);
            prepared.setDouble(3, serviceHours);
            int result = prepared.executeUpdate();
            res = result != 0;
            connect.close();
        } else {
            Message.showErrorMessage("Database connection failed!");
        }
        return res;
    }

    /**
     * method getAllStaffFromDb() to get the list of all staff in the database
     *
     * @return a ObservableList of all Staff
     */
    public static ObservableList<Staff> getAllStaffFromDb() {
        connect = getConnection();
        ObservableList<Staff> listStaff = FXCollections.observableArrayList();
        if (connect != null) {
            try {
                String sql = "SELECT * FROM staff";
                PreparedStatement prepared = connect.prepareStatement(sql);
                ResultSet resultSet = prepared.executeQuery();
                while (resultSet.next()) {
                    Staff staff;
                    String id = resultSet.getString("ID");
                    String name = resultSet.getString("Name");
                    String phone = resultSet.getString("Phone");
                    String type = resultSet.getString("Type");
                    Gender gender = Gender.valueOf(resultSet.getString("Gender"));
                    LocalDate dob = resultSet.getDate("Birthday").toLocalDate();
                    int researchPapers = resultSet.getInt("ResearchPapers");
                    double teachingHours = resultSet.getDouble("TeachingHours");
                    double serviceHours = resultSet.getDouble("ServiceHours");

                    Path path = Paths.get("/tmp/img/");
                    Files.createDirectories(path);
                    String imagePath = path + "/" + id;
                    try (FileOutputStream fos = new FileOutputStream(imagePath)) {
                        Blob blob = resultSet.getBlob("Image");
                        int len = (int) blob.length();
                        byte[] buf = blob.getBytes(1, len);
                        fos.write(buf, 0, len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (type.equals("Teaching")) {
                        staff = new TeachingStaff(name, id, dob, teachingHours, phone, gender);
                    } else if (type.equals("Service")) {
                        staff = new ServiceStaff(name, id, dob, serviceHours, phone, gender);
                    } else {
                        staff = new ResearchStaff(name, id, dob, researchPapers, phone, gender);
                    }
                    listStaff.add(staff);
                }
                connect.close();
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return listStaff;
    }

    /**
     * method addStaff() user to add a staff and image to the database
     *
     * @param staff     a staff you want to add
     * @param imagePath path of the image of staff
     * @return true if add staff successfully, false otherwise
     */
    public static boolean addStaff(Staff staff, String imagePath) {
        try {
            connect = getConnection();
            if (connect != null) {
                String sql = "INSERT INTO `staff`(`ID`, `Name`, `Gender`, `Birthday`, `Phone`, `Type`, " +
                        "`TeachingHours`, `ServiceHours`, `ResearchPapers`, `Image`) VALUES (?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement prepared = connect.prepareStatement(sql);
                prepared.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(staff.getId())));
                prepared.setString(2, staff.getName());
                prepared.setString(3, staff.getGender().toString());
                prepared.setDate(4, Date.valueOf(staff.getDob()));
                prepared.setString(5, staff.getPhone());
                if (staff.getType() == TypeStaff.TeachingStaff) {
                    prepared.setString(6, "Teaching");
                    prepared.setDouble(7, ((TeachingStaff) staff).getTeachingHours());
                    prepared.setDouble(8, 0);
                    prepared.setInt(9, 0);
                } else if (staff.getType() == TypeStaff.ResearchStaff) {
                    prepared.setString(6, "Research");
                    prepared.setDouble(7, 0);
                    prepared.setDouble(8, 0);
                    prepared.setInt(9, ((ResearchStaff) staff).getResearchPapers());
                } else if (staff.getType() == TypeStaff.ServiceStaff) {
                    prepared.setString(6, "Service");
                    prepared.setDouble(7, 0);
                    prepared.setDouble(8, ((ServiceStaff) staff).getServiceHours());
                    prepared.setInt(9, 0);
                }
                InputStream in = new FileInputStream(imagePath);
                prepared.setBlob(10, in);
                int result = prepared.executeUpdate();
                if (result != 0) {
                    return true;
                }
                connect.close();
            }
        } catch (SQLException e) {
            Message.showErrorMessage(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * method deleteStaff() used to delete a staff in the database by their id
     *
     * @param id ID of staff you want to delete
     * @return true if detele staff sucessfully, false otherwise
     */
    public static boolean deleteStaff(String id) {
        try {
            connect = getConnection();
            if (connect != null) {
                String sql = "DELETE FROM staff WHERE ID=?";
                PreparedStatement prepared = connect.prepareStatement(sql);
                prepared.setString(1, id);
                if (prepared.executeUpdate() > 0) {
                    return true;
                }
                connect.close();
            }
        } catch (SQLException e) {
            Message.showErrorMessage(e.getMessage());
        }
        return false;
    }

    /**
     * method updateStaff() used to update a exist staff in the datebase
     *
     * @param staff     staff has information you will update
     * @param imagePath path of new image of staff
     * @return true if update staff sucessfully, false otherwise
     */
    public static boolean updateStaff(Staff staff, String imagePath) {
        try {
            connect = getConnection();
            if (connect != null) {
                String sql = "UPDATE `staff` SET `Name`=?,`Gender`=?, `Birthday`=?,`Phone`=?,`Type`=?," +
                        "`TeachingHours`=?, `ServiceHours`=?,`ResearchPapers`=?,`Image`=? WHERE `ID`=?";
                PreparedStatement prepared = connect.prepareStatement(sql);
                prepared.setString(1, staff.getName());
                prepared.setString(2, staff.getGender().toString());
                prepared.setDate(3, Date.valueOf(staff.getDob()));
                prepared.setString(4, staff.getPhone());
                if (staff.getType() == TypeStaff.TeachingStaff) {
                    prepared.setString(5, "Teaching");
                    prepared.setDouble(6, ((TeachingStaff) staff).getTeachingHours());
                    prepared.setDouble(7, 0);
                    prepared.setInt(8, 0);
                } else if (staff.getType() == TypeStaff.ResearchStaff) {
                    prepared.setString(5, "Research");
                    prepared.setDouble(6, 0);
                    prepared.setDouble(7, 0);
                    prepared.setInt(8, ((ResearchStaff) staff).getResearchPapers());
                } else if (staff.getType() == TypeStaff.ServiceStaff) {
                    prepared.setString(5, "Service");
                    prepared.setDouble(6, 0);
                    prepared.setDouble(7, ((ServiceStaff) staff).getServiceHours());
                    prepared.setInt(8, 0);
                }
                InputStream in = new FileInputStream(imagePath);
                prepared.setBlob(9, in);
                prepared.setString(10, staff.getId());
                int result = prepared.executeUpdate();
                if (result != 0) {
                    return true;
                }
                connect.close();
            }
        } catch (SQLException e) {
            Message.showErrorMessage(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * method getStandard() used to get information of the standard indies about standard teaching hours, standard
     * service hours and standard research papers in the database
     */
    public static void getStandard() {
        try {
            connect = getConnection();
            if (connect != null) {
                String sql = "SELECT * FROM standard";
                PreparedStatement prepared = connect.prepareStatement(sql);
                ResultSet resultSet = prepared.executeQuery();
                while (resultSet.next()) {
                    TeachingStaff.setStandardTeachingHours(resultSet.getDouble("TeachingHours"));
                    ServiceStaff.setStandardServiceHours(resultSet.getDouble("ServiceHours"));
                    ResearchStaff.setStandardResearchPapers(resultSet.getInt("ResearchPapers"));
                }
                connect.close();
            }
        } catch (SQLException e) {
            Message.showErrorMessage(e.getMessage());
        }
    }
}
