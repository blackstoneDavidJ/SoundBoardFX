package com.example.soundboardfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class HelloController
{
    @FXML
    private TextArea descriptionTextBox;
    @FXML
    private TextField creatorTextBox;
    @FXML
    private Button submitButton;
    @FXML
    private Label soundBoardTitleLabel;
    @FXML
    private DatePicker dateTextBox;
    @FXML
    private TextField titleTextBox;
    @FXML
    private ProgressBar recordProgressBar;
    @FXML
    private Button recordPauseButton;
    @FXML
    private Button recordPlayButton;
    @FXML
    private Button recordStopButton;
    @FXML
    private Label welcomeText;
    @FXML
    private ListView<String> soundListView;
    @FXML
    private ObservableList<String> soundList;
    private double progress = 0.0;
    public HelloController() {
        // Initialize the soundList
        soundList = FXCollections.observableArrayList();
        //soundList.addAll();
    }
    @FXML
    protected void submitNewSoundPack()
    {
        Thread progressThread = new Thread(() -> {
            while(progress < 1.0)
            {
                progress+=0.01;
                recordProgressBar.setProgress(progress);
                System.out.println("Progress: " + progress);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        String author = creatorTextBox.getText();
        String title = titleTextBox.getText();
        String date = dateTextBox.getEditor().getText();
        String description = descriptionTextBox.getText();
        soundList.add(title);
        soundListView.setItems(soundList); // Update the ListView
    }

    public void onSoundClicked(MouseEvent mouseEvent)
    {
        System.out.println("Clicked on " + soundListView.getSelectionModel().getSelectedItem());
    }
}
