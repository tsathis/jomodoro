module com.github.tharindusathis {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.logging;

    opens com.github.tharindusathis.jomodoro.controller to javafx.fxml;
    exports com.github.tharindusathis.jomodoro;
}