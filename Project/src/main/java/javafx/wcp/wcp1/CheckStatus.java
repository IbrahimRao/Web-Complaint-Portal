package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckStatus {

    @FXML
    private TextField idField;

    @FXML
    private Label complaintStatus;

    private Stage stage;

    public void setStage(Stage statusStage) {
        this.stage = statusStage;
        statusStage.setWidth(700);
        statusStage.setHeight(600);
    }

    public void handleSubmitButton(ActionEvent actionEvent) {
        String complaintID = idField.getText();

        if (!complaintID.isEmpty()) {
            try {
                int compId = Integer.parseInt(complaintID);

                // Fetch status from ckStatus table
                String status = fetchStatusFromCkStatus(compId);

                // Display the fetched status
                complaintStatus.setText("Complaint Status: " + status);

            } catch (NumberFormatException e) {
                e.printStackTrace();
                complaintStatus.setText("Invalid Complaint ID.");
            }
        } else {
            complaintStatus.setText("Please enter Complaint ID.");
        }
    }

    private String fetchStatusFromCkStatus(int compId) {
        try {
            Connection connection = DatabaseConnector.connect();

            // SQL query to fetch status from ckStatus table
            String fetchStatusQuery = "SELECT Status FROM ckStatus WHERE CID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(fetchStatusQuery)) {
                preparedStatement.setInt(1, compId);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if any result is returned
                if (resultSet.next()) {
                    return resultSet.getString("Status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Status not found"; // Return a default value if status is not found
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
