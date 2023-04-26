module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
    requires java.logging;
    requires com.opencsv;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}