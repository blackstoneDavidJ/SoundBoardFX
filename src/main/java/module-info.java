module com.example.soundboardfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens SoundBoardFX to javafx.fxml;
    exports SoundBoardFX;
}