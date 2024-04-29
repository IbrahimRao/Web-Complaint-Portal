package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateComplaint {

    @FXML
    private TextField idField;

    @FXML
    private TextField forField;

    @FXML
    private TextField complaintField;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setWidth(700);
        stage.setHeight(600);
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        // Keep prompting until a unique ID is entered
        int id;
        do {
            id = promptForUniqueID();
        } while (id == -1); // -1 indicates the ID is not unique

        // Get the values from the text fields
        String forValue = forField.getText();
        String complaint = complaintField.getText();

        // Insert the values into the MySQL database
        insertIntoDatabase(id, forValue, complaint);

        // Handle any further actions or close the stage as needed
        System.out.println("Complaint Submitted!");
        stage.close();
    }

    private int promptForUniqueID() {
        int id = -1;

        do {
            try {
                // Parse the entered ID
                id = Integer.parseInt(idField.getText());

                // Check if the ID already exists in the database
                if (isIDAlreadyExists(id)) {
                    // Show a warning message for duplicate ID
                    System.out.println("ID already exists. Please enter a unique ID.");

                    // Clear the ID field for re-entry
                    idField.clear();
                }
            } catch (NumberFormatException e) {
                // Show a warning message for invalid input
                System.out.println("Please enter a valid integer for the ID.");

                // Clear the ID field for re-entry
                idField.clear();
            }
        } while (id == -1);

        return id;
    }

    private boolean isIDAlreadyExists(int id) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // Create a prepared statement to check if the ID exists
            String query = "SELECT COUNT(*) FROM complaint WHERE ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void insertIntoDatabase(int id, String forValue, String complaint) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // Create a prepared statement for inserting into the complaints table
            String insertComplaintQuery = "INSERT INTO complaint (`ID`, `For`, `Complaints`) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertComplaintQuery)) {
                // Set the parameter values
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, forValue);
                preparedStatement.setString(3, complaint);

                // Execute the query
                preparedStatement.executeUpdate();
            }

            // Check the status in the assignComplaints table
            String assignComplaintsQuery = "SELECT Status FROM assignComplaints WHERE ComplaintID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(assignComplaintsQuery)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String status = resultSet.getString("Status");

                        // Update the ckStatus table based on the status
                        updateCkStatusTable(id, status);
                    } else {
                        // No entry found in assignComplaints, set status to 'Pending'
                        updateCkStatusTable(id, "Pending");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the database connection
                DatabaseConnector.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateCkStatusTable(int id, String status) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // Create a prepared statement for updating ckStatus table
            String updateCkStatusQuery = "INSERT INTO ckStatus (`CID`, `Status`) VALUES (?, ?)" +
                    "ON DUPLICATE KEY UPDATE `Status` = VALUES(`Status`)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateCkStatusQuery)) {
                // Set the parameter values
                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, status);

                // Execute the query
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the main home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("customer-home.fxml"));

            // Load the root node (the entire scene) of the main home page
            Parent root = loader.load();

            // Get the controller associated with the main home page
            CustomerHome customerController = loader.getController();

            // Pass the stage to the HelloController
            customerController.setStage(stage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
