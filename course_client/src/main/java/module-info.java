module org.example.course_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens org.example.course_client to javafx.fxml;
    exports org.example.course_client;
    exports com.example.cource_client;
    opens com.example.cource_client to javafx.fxml;
}