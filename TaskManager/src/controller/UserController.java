package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserController {

    @FXML
    private Label lbl_header;
    public void initialize(){
        lbl_header.setText(lbl_header.getText()+LoginController.id_u);
    }
}
