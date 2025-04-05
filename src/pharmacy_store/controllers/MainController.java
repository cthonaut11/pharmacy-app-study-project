package pharmacy_store.controllers;

import javafx.fxml.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class MainController {

    @FXML
    private Button button;

    @FXML
    private void click(ActionEvent event){
        button.setText("You've clicked");
    }
}
