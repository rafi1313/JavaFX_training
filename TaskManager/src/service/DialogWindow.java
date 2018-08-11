package service;

import javafx.scene.control.Alert;

public class DialogWindow {
    public DialogWindow(Alert.AlertType alertType, String title, String header, String content) {
        Alert a = new Alert(alertType);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}
