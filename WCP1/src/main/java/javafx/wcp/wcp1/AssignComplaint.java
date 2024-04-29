package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssignComplaint {

    @FXML
    private TextField nameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField statusField;

    @FXML
    private Button submitButton;

    private Stage stage;

    public void setStage(Stage assignStage) {
        this.stage = assignStage;
        stage.setWidth(500);
        stage.setHeight(400);
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        String complaintId = idField.getText();
        String officerName = nameField.getText();
        String status = statusField.getText();

        if (!complaintId.isEmpty() && !officerName.isEmpty() && !status.isEmpty()) {
            int id = Integer.parseInt(complaintId);

            if (complaintExists(id)) {
                // If complaint exists, insert into the assignComplaints table
                int assignId = insertAssignComplaint(id, officerName, status);

                // Update the status in the ckStatus table
                updateCkStatusTable(id);

                // Display the generated AssignID
                showAssignId(assignId);
            } else {
                // Complaint does not exist
                showAlert("Complaint not found", "The specified Complaint ID does not exist.");
            }
        } else {
            // Fields are not filled
            showAlert("Missing Information", "Please fill in all the fields.");
        }
    }

    private boolean complaintExists(int complaintId) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // SQL query to check if the complaint exists
            String selectQuery = "SELECT * FROM complaint WHERE ID = ?";

            // Create a PreparedStatement with the query
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, complaintId);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any result is returned
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int insertAssignComplaint(int complaintId, String officerName, String status) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // SQL query to insert into assignComplaints table
            String insertQuery = "INSERT INTO assignComplaints (ComplaintID, Name, Status) VALUES (?, ?, ?)";

            // Create a PreparedStatement with the query
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, complaintId);
                preparedStatement.setString(2, officerName);
                preparedStatement.setString(3, status);

                // Execute the insert statement
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Get the generated AssignID
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1; // Return -1 if insertion fails
    }

    private void updateCkStatusTable(int id) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // Check the status in the assignComplaints table
            String assignComplaintsQuery = "SELECT Status FROM assignComplaints WHERE ComplaintID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(assignComplaintsQuery)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String status = resultSet.getString("Status");

                        // Update the ckStatus table based on the status
                        String updateCkStatusQuery = "UPDATE ckStatus SET Status = 'Assigned' WHERE CID = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateCkStatusQuery)) {
                            updateStatement.setInt(1, id);
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAssignId(int assignId) {
        // Display the generated AssignID
        showAlert("Assign ID", "Assign ID: " + assignId);
    }

    private void showAlert(String title, String content) {
        // Display an alert with the specified title and content
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
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
