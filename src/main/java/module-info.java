module com.mycompany.g17.wordageddon {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.g17.wordageddon to javafx.fxml;
    exports com.mycompany.g17.wordageddon;
}
