module com.example.semestral_work {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.semestral_work to javafx.fxml;
    exports com.example.semestral_work;
}