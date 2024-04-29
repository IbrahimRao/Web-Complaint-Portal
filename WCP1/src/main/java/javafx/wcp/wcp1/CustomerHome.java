package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerHome {

    private Stage stage;

    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setWidth(500);
        stage.setHeight(400);
    }



    public void handleCreateComplaint(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create-complaint.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            CreateComplaint complaintController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage complaintStage = new Stage();
            complaintController.setStage(complaintStage);

            Scene scene = new Scene(root);
            complaintStage.setScene(scene);

            // Show the new stage
            complaintStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleCheckStatus(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("check-status.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            CheckStatus statusController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage statusStage = new Stage();
            statusController.setStage(statusStage);

            Scene scene = new Scene(root);
            statusStage.setScene(scene);

            // Show the new stage
            statusStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Implement this method as needed
    }

    public void handleCancelComplaint(ActionEvent actionEvent) {
        // Implement this method as needed
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("close-complaint.fxml"));
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            CloseComplaint closeController = loader.getController();

            // Create a new stage for the CreateComplaint controller
            Stage closeStage = new Stage();
            closeController.setStage(closeStage);

            Scene scene = new Scene(root);
            closeStage.setScene(scene);

            // Show the new stage
            closeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @FXML
//    public void handleBackButton(ActionEvent actionEvent) {
//        try {
//            // Load the FXML file for the main home page
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
//
//            // Load the root node (the entire scene) of the main home page
//            Parent root = loader.load();
//
//            Login loginController = loader.getController();
//
//            Stage loginStage = new Stage();
//            loginController.setStage(loginStage);
//
//            // Create a new scene with the loaded root node
//            Scene scene = new Scene(root);
//            loginStage.setScene(scene);
//
//            // Show the new stage
//            loginStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the main home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));

            // Load the root node (the entire scene) of the main home page
            Parent root = loader.load();

            Login loginController = loader.getController();

            Stage loginStage = new Stage();
            loginController.setStage(loginStage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);
            loginStage.setScene(scene);

            // Show the new stage
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
