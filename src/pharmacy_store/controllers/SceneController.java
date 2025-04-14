package pharmacy_store.controllers;

import javafx.fxml.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    private Stage stage;
    private Scene scene;
    public Parent root;

    @FXML
    private Button button;

    @FXML
    public void switchToSceneStorage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/pharmacy_store/resources/Storage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSceneMain(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/Main.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSceneCustomers(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("../resources/Customers.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}