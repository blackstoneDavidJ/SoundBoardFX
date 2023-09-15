package SoundBoardFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static final int SCREEN_WIDTH  = 1280;
    private static final int SCREEN_HEIGHT = 720;
    private static final String PANEL_FILE  = "SoundBoardPanel.fxml";
    private static final String PANEL_TITLE = "Sound Board <3 :)";
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(PANEL_FILE));
        Scene scene = new Scene(fxmlLoader.load(), SCREEN_WIDTH, SCREEN_HEIGHT);
        stage.setResizable(false);
        stage.setTitle(PANEL_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}