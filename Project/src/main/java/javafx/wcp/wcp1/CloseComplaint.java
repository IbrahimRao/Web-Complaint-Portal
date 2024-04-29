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

public class CloseComplaint {

    @FXML
    private TextField idField;

    @FXML
    private TextField forField;

    private Stage stage;

    public void setStage(Stage statusStage) {
        this.stage = statusStage;
        statusStage.setWidth(700);
        statusStage.setHeight(600);
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        String complaintId = idField.getText();
        String complaintFor = forField.getText();

        if (!complaintId.isEmpty() && !complaintFor.isEmpty()) {
            int id = Integer.parseInt(complaintId);
            if (deleteComplaint(id, complaintFor)) {
                System.out.println("Complaint Cancelled");
            } else {
                System.out.println("Error cancelling complaint");
            }
        }
    }

    private boolean deleteComplaint(int complaintId, String complaintFor) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // Check if the complaint ID exists in the assignComplaints table
            if (complaintExistsInAssignComplaints(complaintId)) {
                // Delete from closedComplaints table
                String deleteClosedComplaintsQuery = "DELETE FROM closedComplaints WHERE AssignedID IN (SELECT AssignID FROM assignComplaints WHERE ComplaintID = ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteClosedComplaintsQuery)) {
                    preparedStatement.setInt(1, complaintId);
                    preparedStatement.executeUpdate();
                }

                // Delete from assignComplaints table
                String deleteAssignComplaintsQuery = "DELETE FROM assignComplaints WHERE ComplaintID = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteAssignComplaintsQuery)) {
                    preparedStatement.setInt(1, complaintId);
                    preparedStatement.executeUpdate();
                }
            }

            // Delete from ckStatus table
            String deleteCkStatusQuery = "DELETE FROM ckStatus WHERE CID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteCkStatusQuery)) {
                preparedStatement.setInt(1, complaintId);
                preparedStatement.executeUpdate();
            }

            // Delete from complaint table
            String deleteComplaintQuery = "DELETE FROM complaint WHERE ID = ? AND `For` = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteComplaintQuery)) {
                preparedStatement.setInt(1, complaintId);
                preparedStatement.setString(2, complaintFor);
                preparedStatement.executeUpdate();
            }

            return true; // Deletion successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean complaintExistsInAssignComplaints(int complaintId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to check if the complaint ID exists in assignComplaints table
            String checkQuery = "SELECT 1 FROM assignComplaints WHERE ComplaintID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
                preparedStatement.setInt(1, complaintId);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean complaintExistsInClosedComplaints(int complaintId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to check if the complaint ID exists in closedcomplaints table
            String checkQuery = "SELECT 1 FROM closedcomplaints WHERE AssignedID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {
                preparedStatement.setInt(1, complaintId);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
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
