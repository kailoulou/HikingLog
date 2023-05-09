module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.logging;
    requires com.opencsv;
    requires com.sothawo.mapjfx;
    requires org.controlsfx.controls;


    opens com.example.demo to javafx.fxml, javafx.graphics;
    exports com.example.demo;
}