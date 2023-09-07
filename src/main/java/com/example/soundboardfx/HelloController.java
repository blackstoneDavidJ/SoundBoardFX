package com.example.soundboardfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class HelloController {
    public Button submitSoundPackButton;
    @FXML
    private Label welcomeText;
    @FXML
    private ListView<String> soundListView;
    private ObservableList<String> soundList;

    public HelloController() {
        // Initialize the soundList
        soundList = FXCollections.observableArrayList();
        soundList.addAll();
    }
    @FXML
    protected void submitNewSoundPack() {
        soundList.add(String.valueOf(soundList.size() + 1));
        soundListView.setItems(soundList); // Update the ListView
    }

    public void onSoundClicked(MouseEvent mouseEvent)
    {
        System.out.println("Clicked on " + soundListView.getSelectionModel().getSelectedItem());
    }
}
