package javafx.wcp.wcp1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Login {

    @FXML
    private Stage stage;

    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;

            stage.setWidth(500);
            stage.setHeight(400);
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLoginButton() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Successful login
                    errorLabel.setText("Login Successful");
                    errorLabel.setStyle("-fx-text-fill: green;");

                    loadHomePage();
                } else {
                    // Failed login
                    errorLabel.setText("Invalid username or password");
                    errorLabel.setStyle("-fx-text-fill: red;");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Handle database connection errors or IO exceptions
        }
    }

    private void loadHomePage() throws IOException {
        Parent homePage = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("customer-home.fxml")));
        Scene scene = new Scene(homePage);

        // Get the current stage
        Stage stage = (Stage) usernameField.getScene().getWindow();

        // Set the new scene
        stage.setScene(scene);
    }

    private boolean isValidLogin(String username, String password) {
        // Implement your authentication logic here
        // For simplicity, you can hardcode a username and password for testing
        return username.equals("") && password.equals("");
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the main home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

            // Load the root node (the entire scene) of the main home page
            Parent root = loader.load();

            // Get the controller associated with the main home page
            HelloController helloController = loader.getController();

            // Pass the stage to the HelloController
            helloController.setStage(stage);

            // Create a new scene with the loaded root node
            Scene scene = new Scene(root);

            // Set the scene to the stage
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
