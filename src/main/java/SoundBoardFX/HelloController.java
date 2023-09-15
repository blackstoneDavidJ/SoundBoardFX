package SoundBoardFX;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;

public class HelloController {
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

    private double progress;

    public HelloController() {
        // Initialize the soundList
        soundList = FXCollections.observableArrayList();
        progress = 0.0;
        //soundList.addAll();
    }

    @FXML
    protected void submitNewSoundPack() {
        Thread backgroundThread = new Thread(() -> {
            while (progress < 1.0) {
                progress += 0.01;

                // Update the progress bar on the JavaFX Application thread
                Platform.runLater(() -> {
                    recordProgressBar.setProgress(progress);
                    System.out.println("Progress: " + progress);
                });

                try {
                    // Sleep for one second (1000 milliseconds)
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Continue with the rest of your code
            String author = creatorTextBox.getText();
            String title = titleTextBox.getText();
            String date = dateTextBox.getEditor().getText();
            String description = descriptionTextBox.getText();
            soundList.add(title);

            // Update the ListView on the JavaFX Application thread
            Platform.runLater(() -> {
                soundListView.setItems(soundList);
            });
        });

        backgroundThread.start();
    }


    public void onSoundClicked(MouseEvent mouseEvent) {
        System.out.println("Clicked on " + soundListView.getSelectionModel().getSelectedItem());
    }
}