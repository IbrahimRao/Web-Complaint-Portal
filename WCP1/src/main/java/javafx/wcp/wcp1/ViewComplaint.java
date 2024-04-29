package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewComplaint {

    public Button submitButton;
    @FXML
    private TextField idField;

    @FXML
    private Label forLabel;

    @FXML
    private Label complaintLabel;

    private Stage stage;

    public void setStage(Stage viewStage) {
        this.stage = viewStage;
        stage.setWidth(500);
        stage.setHeight(400);
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        String complaintId = idField.getText();
        if (!complaintId.isEmpty()) {
            int id = Integer.parseInt(complaintId);
            displayComplaintDetails(id);
        }
    }

    private void displayComplaintDetails(int complaintId) {
        try {
            // Establish a database connection
            Connection connection = DatabaseConnector.connect();

            // SQL query to select the complaint based on ID
            String selectQuery = "SELECT `For`, Complaints FROM complaint WHERE ID = ?";

            // Create a PreparedStatement with the query
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, complaintId);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any result is returned
                if (resultSet.next()) {
                    String forValue = resultSet.getString("For");
                    String complaintValue = resultSet.getString("Complaints");

                    // Display the values in the labels
                    forLabel.setText("For: " + forValue);
                    complaintLabel.setText("Complaint: " + complaintValue);
                } else {
                    // No entry found for the given ID
                    forLabel.setText("No entry found for ID: " + complaintId);
                    complaintLabel.setText("");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
