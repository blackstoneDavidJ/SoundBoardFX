module com.example.soundboardfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.soundboardfx to javafx.fxml;
    exports com.example.soundboardfx;
}