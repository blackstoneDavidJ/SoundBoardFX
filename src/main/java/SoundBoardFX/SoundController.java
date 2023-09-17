package SoundBoardFX;

import Recorder.SoundRecorder;
import com.example.soundboardfx.SoundFileManager;
import com.example.soundboardfx.SoundPack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.application.Platform;
import javafx.scene.paint.Paint;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class SoundController {
    @FXML
    private TextField folderPathTextField;
    @FXML
    private Button folderSubmitButton;
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
    private final SoundRecorder recorder;
    private double progress;
    private boolean stoppedRecording;
    private static Thread progressThread;
    private File currentWavFile;
    private final HashMap<String, Object[]> soundFileMap;
    public SoundController() throws LineUnavailableException {
        String folderPath = "C://Users//dblac//OneDrive//Desktop//soundTest";
        // Initialize the soundList
        recorder = new SoundRecorder(folderPath);
        soundList = FXCollections.observableArrayList();
        soundFileMap = new HashMap<>();

    }

    @FXML
    protected void submitNewSoundPack() {

        // Continue with the rest of your code
        String author = creatorTextBox.getText();
        String title = titleTextBox.getText();
        String date = dateTextBox.getEditor().getText();
        String description = descriptionTextBox.getText();

        if(!author.isEmpty() && !title.isEmpty() && !date.isEmpty() && !description.isEmpty() && currentWavFile != null)
        {
            SoundPack soundPack = new SoundPack(author, title, date, description, Paths.get(currentWavFile.getAbsolutePath()).getFileName().toString());
            SoundFileManager.writeSoundPackToFile(soundPack, currentWavFile.getAbsolutePath());
            soundList.add(title);
            Platform.runLater(() -> soundListView.setItems(soundList));
            submitButton.setText("✔");
            submitButton.setTextFill(Paint.valueOf("#69da12"));

            addSoundFilesToMap(new File("C://Users//dblac//OneDrive//Desktop//soundTest"));
            soundList.addAll();
        }

        else {
            submitButton.setText("✘");
            submitButton.setTextFill(Paint.valueOf("#d70000"));
        }
    }


    public void onSoundClicked(MouseEvent mouseEvent) {
        System.out.println("Clicked on " + soundListView.getSelectionModel().getSelectedItem());
    }

    public void onRecordButtonClicked(MouseEvent mouseEvent) {
        recorder.record(titleTextBox.getText());
        stoppedRecording = false;
        progress = 0.0;
        recordProgressBar.setProgress(progress);
        progressThread = new Thread(() -> {
            while (progress < 1.0 && !stoppedRecording) {
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
                    System.out.println("sleep interupted");
                }
            }
        });

        progressThread.start();
    }
    public void onStopRecordingButton(ActionEvent actionEvent) throws InterruptedException {
        currentWavFile = recorder.stopRecording();
        stoppedRecording = true;
        progressThread.interrupt();
    }

    public void onFolderPathSubmit(ActionEvent actionEvent)
    {
        if (folderPathTextField.getText().isEmpty()) {
            folderSubmitButton.setText("✘");
            folderSubmitButton.setTextFill(Paint.valueOf("#d70000"));
        } else {
            SoundFileManager.saveSoundPackFolderPath("soundPackFolder.txt",folderPathTextField.getText());
            folderSubmitButton.setText("✔");
            folderSubmitButton.setTextFill(Paint.valueOf("#69da12"));
        }

    }

    public void onFolderTextChange(MouseEvent actionEvent)
    {
        folderSubmitButton.setText("Change Folder");
        folderSubmitButton.setTextFill(Paint.valueOf("black"));
    }

    public void onTitleChanged(MouseEvent mouseEvent)
    {
        submitButton.setText("Submit Sound");
        submitButton.setTextFill(Paint.valueOf("black"));
    }

    private void addSoundFilesToMap(final File folder) {
        SoundPack newSound = null;
        File tmpWaveFile = null;
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                addSoundFilesToMap(fileEntry);
            }

            else {
                if(fileEntry.getAbsolutePath().contains(".wav.spo"))
                {
                    newSound = SoundFileManager.readSoundPackFromFile(fileEntry);
                }

                else if(fileEntry.getAbsolutePath().contains(".wav") && !fileEntry.getAbsolutePath().contains(".spo"))
                {
                    tmpWaveFile = fileEntry;
                }

                soundFileMap.put(newSound.title(), new Object[] {newSound, tmpWaveFile});
            }
        }

        System.out.println("Refreshed Listing from file system.");
    }
}