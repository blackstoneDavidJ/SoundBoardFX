package SoundBoardFX;

import Recorder.SoundPlayer;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Objects;

public class SoundController {
    @FXML
    private Label creatorLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Button playSelectedSoundButton;
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
    private SoundRecorder recorder;
    private double progress;
    private boolean stoppedRecording;
    private static Thread progressThread;
    private File currentWavFile;
    private HashMap<String, Object[]> soundFileMap;

    private String folderPath;
    private File wavFileSelected;
    private static final String soundPackFilePath = "soundPackFile.txt";

    public SoundController() throws LineUnavailableException {
        Path path = Path.of(soundPackFilePath);
        if (!Files.exists(path))
        {
            System.out.println("Sound pack file does not exist, let's create one.");
            try {
                Files.createFile(path);
                System.out.println("Success!");
            } catch (IOException e) {
                System.out.println("Failed!");
                throw new RuntimeException(e);
            }
        }

        folderPath = SoundFileManager.readSoundPackFolderPath(soundPackFilePath);
        recorder = new SoundRecorder(folderPath);
        soundList = FXCollections.observableArrayList();
        soundFileMap = new HashMap<>();
        addSoundFilesToMap(new File(folderPath));
        Platform.runLater(() -> soundListView.setItems(soundList));
    }

    @FXML
    protected void submitNewSoundPack() {

        // Continue with the rest of your code
        String author = creatorTextBox.getText();
        String title = titleTextBox.getText();
        String date = dateTextBox.getEditor().getText();
        String description = descriptionTextBox.getText();

        if(!author.isEmpty() && !title.isEmpty() && !date.isEmpty() && currentWavFile != null)
        {
            SoundPack soundPack = new SoundPack(author, title, date, description, Paths.get(currentWavFile.getAbsolutePath()).getFileName().toString());
            SoundFileManager.writeSoundPackToFile(soundPack, currentWavFile.getAbsolutePath());
            submitButton.setText("✔");
            submitButton.setTextFill(Paint.valueOf("#69da12"));
            addSoundFilesToMap(new File(folderPath));
        }

        else {
            submitButton.setText("✘");
            submitButton.setTextFill(Paint.valueOf("#d70000"));
        }
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
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("sleep interrupted");
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
            SoundFileManager.saveSoundPackFolderPath(soundPackFilePath,folderPathTextField.getText());
            folderPath = SoundFileManager.readSoundPackFolderPath("");
            folderSubmitButton.setText("✔");
            folderSubmitButton.setTextFill(Paint.valueOf("#69da12"));
            refreshListView();
        }

    }

    private void refreshListView()
    {
        folderPath = SoundFileManager.readSoundPackFolderPath(soundPackFilePath);
        try {
            recorder = new SoundRecorder(folderPath);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        soundList = FXCollections.observableArrayList();
        soundFileMap = new HashMap<>();
        addSoundFilesToMap(new File(folderPath));
        Platform.runLater(() -> soundListView.setItems(soundList));
    }
    public void onFolderTextChange(MouseEvent actionEvent)
    {
        folderSubmitButton.setText("Change Folder");
        folderSubmitButton.setTextFill(Paint.valueOf("black"));
        recordProgressBar.setProgress(0.0);
    }

    public void onTitleChanged(MouseEvent mouseEvent)
    {
        submitButton.setText("Submit Sound");
        submitButton.setTextFill(Paint.valueOf("black"));
    }

    private void addSoundFilesToMap(final File folder) {
        SoundPack newSound = null;
        File tmpWaveFile = null;

        // Check if the folder exists and is a directory.
        if (!folder.exists() || !folder.isDirectory()) {
            // Handle the case where the folder doesn't exist or is not a directory.
            return;
        }

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                // Recursively process subdirectories.
                addSoundFilesToMap(fileEntry);
            } else {
                if (fileEntry.getAbsolutePath().endsWith(".spo") ) {
                    newSound = SoundFileManager.readSoundPackFromFile(fileEntry);
                } else if (fileEntry.getAbsolutePath().endsWith(".wav") && !fileEntry.getAbsolutePath().endsWith(".spo")) {
                    tmpWaveFile = fileEntry;
                }

                if (newSound != null && tmpWaveFile != null && !soundFileMap.containsKey(newSound.title())) {
                    soundFileMap.put(newSound.title(), new Object[] { newSound, tmpWaveFile });
                    soundList.add(newSound.title());

                    // Reset the variables for the next iteration.
                    newSound = null;
                    tmpWaveFile = null;
                }
            }
        }

        System.out.println("Refreshed Listing from file system.");
    }


    public void onSoundClicked(MouseEvent mouseEvent)
    {
        String soundSelected = soundListView.getSelectionModel().getSelectedItem();
        SoundPack soundPackSelected = (SoundPack) soundFileMap.get(soundSelected)[0];
        wavFileSelected = (File) soundFileMap.get(soundSelected)[1];

        titleLabel.setText(soundPackSelected.title());
        creatorLabel.setText(soundPackSelected.author());
        dateLabel.setText(soundPackSelected.creationDate());
        descriptionLabel.setText(soundPackSelected.description());
    }

    public void onPlaySelectedSound(ActionEvent actionEvent)
    {
        SoundPlayer player = new SoundPlayer();
        if(wavFileSelected == null)
        {
            playSelectedSoundButton.setText("✘");
            playSelectedSoundButton.setTextFill(Paint.valueOf("#d70000"));
        }
        else {
            new Thread(() -> {
                player.play(wavFileSelected);
            }).start();
        }

    }
}