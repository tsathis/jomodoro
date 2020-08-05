module com.github.tharindusathis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.github.tharindusathis.jomodoro.controller to javafx.fxml;
    exports com.github.tharindusathis.jomodoro;
}