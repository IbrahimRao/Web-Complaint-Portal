package javafx.wcp.wcp1;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelloController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleCustomerLogin() {
        try {
            // Load the FXML file for the second page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));

            // Load the root node (the entire scene) of the second page
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            Login loginController = loader.getController();

            // Pass the stage to the Login controller
            loginController.setStage(stage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAdminLogin(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the second page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));

            // Load the root node (the entire scene) of the second page
            Parent root = loader.load();

            // Get the controller associated with the FXML file
            AdminLogin loginController = loader.getController();

            // Pass the stage to the Login controller
            loginController.setStage(stage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}