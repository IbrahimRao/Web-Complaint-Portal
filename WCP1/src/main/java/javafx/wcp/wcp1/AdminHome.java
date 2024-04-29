package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminHome {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setWidth(500); // Set the width as needed
        stage.setHeight(400); // Set the height as needed
    }
    public void handleViewComplaint(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view-complaint.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            ViewComplaint viewController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage viewStage = new Stage();
            viewController.setStage(viewStage);

            Scene scene = new Scene(root);
            viewStage.setScene(scene);

            // Show the new stage
            viewStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAssignComplaint(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("assign-complaint.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            AssignComplaint assignController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage assignStage = new Stage();
            assignController.setStage(assignStage);

            Scene scene = new Scene(root);
            assignStage.setScene(scene);

            // Show the new stage
            assignStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCloseComplaint(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-close-complaint.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            AdminCloseComplaint adminCloseController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage adminCloseStage = new Stage();
            adminCloseController.setStage(adminCloseStage);

            Scene scene = new Scene(root);
            adminCloseStage.setScene(scene);

            // Show the new stage
            adminCloseStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBackButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the main home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));

            // Load the root node (the entire scene) of the main home page
            Parent root = loader.load();

            AdminLogin adminController = loader.getController();

            Stage adminLoginStage = new Stage();
            adminController.setStage(adminLoginStage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);
            adminLoginStage.setScene(scene);

            // Show the new stage
            adminLoginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
