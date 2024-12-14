module Semestral_Work.src.client {
    // JavaFX dependencies
    requires javafx.controls;
    requires javafx.fxml;

    // Client-side libraries
    requires org.controlsfx.controls;
    requires java.net.http; // HTTP client for networking
    requires org.json; // JSON processing
    requires java.sql; // SQLite for direct database manipulation (if used in Client)
    requires com.google.gson;
    requires java.json; // JSON support for serialization/deserialization

    // Client packages for JavaFX and Gson usage
    exports Client.library to javafx.graphics, javafx.fxml; // Allow graphics and FXML to access this package
    exports Client.library.Controller;
    exports Client.library.model;

    // Open packages for JavaFX reflection and Gson serialization
    opens Client.library to javafx.fxml;
    opens Client.library.Controller to javafx.fxml, com.google.gson;
    opens Client.library.model to com.google.gson;
}