package com.example.soundboardfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private ListView<String> SoundListView;
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

}