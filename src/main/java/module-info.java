module com.example.chatapp_using_socket_programing {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatapp_using_socket_programing to javafx.fxml;
    exports com.example.chatapp_using_socket_programing;
}