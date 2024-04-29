package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminCloseComplaint {

    public TextField idField;
    public TextField complaintField;
    public Button submitButton;
    private Stage stage;

    public void setStage(Stage adminCloseStage) {
        this.stage = adminCloseStage;
        stage.setWidth(500);
        stage.setHeight(400);
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
        String assignID = idField.getText();
        String complaintID = complaintField.getText();

        if (!assignID.isEmpty() && !complaintID.isEmpty()) {
            try {
                int assignId = Integer.parseInt(assignID);
                int compId = Integer.parseInt(complaintID);

                // Check if entries match in assignComplaints and closedComplaints
                if (checkEntriesMatch(assignId, compId)) {
                    // Update status in assignComplaints table
                    updateAssignComplaintStatus(assignId);

                    // Insert into closedComplaints table
                    insertClosedComplaint(assignId, compId);

                    // Update status in ckStatus table
                    updateCkStatusTable(compId);

                    // Show alert
                    showAlert("Complaint Resolved", "The complaint has been resolved.");
                } else {
                    showAlert("Error", "Invalid Assign ID or Complaint ID. Entries do not match.");
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                showAlert("Error", "Invalid Assign ID or Complaint ID.");
            }
        } else {
            showAlert("Error", "Please enter both Assign ID and Complaint ID.");
        }
    }

    private boolean checkEntriesMatch(int assignId, int compId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to check if entries match in assignComplaints and closedComplaints
            String checkQuery = "SELECT 1 FROM assignComplaints WHERE AssignID = ? AND ComplaintID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
                preparedStatement.setInt(1, assignId);
                preparedStatement.setInt(2, compId);

                // Execute the query
                return preparedStatement.executeQuery().next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void updateAssignComplaintStatus(int assignId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to update status in assignComplaints table
            String updateQuery = "UPDATE assignComplaints SET Status = 'Resolved' WHERE AssignID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setInt(1, assignId);

                // Execute the update
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertClosedComplaint(int assignId, int compId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to insert into closedComplaints table
            String insertQuery = "INSERT INTO closedComplaints (AssignedID, CompID, Status) VALUES (?, ?, 'Resolved')";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, assignId);
                preparedStatement.setInt(2, compId);

                // Execute the insert
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCkStatusTable(int compId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to update status in ckStatus table
            String updateCkStatusQuery = "UPDATE ckStatus SET Status = 'Resolved' WHERE CID = ?";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateCkStatusQuery)) {
                updateStatement.setInt(1, compId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the admin home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-home.fxml"));

            // Load the root node (the entire scene) of the admin home page
            Parent root = loader.load();

            // Get the controller associated with the admin home page
            AdminHome adminController = loader.getController();

            // Pass the stage to the AdminHomeController
            adminController.setStage(stage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
