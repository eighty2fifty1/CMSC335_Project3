module com.example.cmsc335_project3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cmsc335_project3 to javafx.fxml;
    exports com.example.cmsc335_project3;
}