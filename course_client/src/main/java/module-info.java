module org.example.course_client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.json;
    requires com.fasterxml.jackson.databind;

    opens org.example.course_client to javafx.fxml;
    exports org.example.course_client;

    opens com.example.course_client.dto to javafx.fxml;
    exports com.example.course_client.dto;

    opens com.example.course_client.service to javafx.fxml;
    exports com.example.course_client.service;

    opens com.example.course_client.controller to javafx.fxml;
    exports com.example.course_client.controller;
}
