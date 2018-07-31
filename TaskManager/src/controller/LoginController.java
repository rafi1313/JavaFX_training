package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.DBConnect;
import service.DialogWindow;
import service.LoginCounter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginController {
    @FXML
    private TextField tf_login;
    @FXML
    private TextField tf_password;
    @FXML
    private PasswordField pf_password;
    @FXML
    private CheckBox cb_show;
    @FXML
    private Button btn_login;

    private String password;
    static int id_u = 0;
    @FXML
    void showPasswordAction(MouseEvent event) {
        if (cb_show.isSelected()) {
            System.out.println("Zaznaczony");
            tf_password.setText(pf_password.getText());
            tf_password.setVisible(true);
            pf_password.setVisible(false);
            pf_password.clear();
        } else {
            System.out.println("Odznaczony");
            pf_password.setText(tf_password.getText());
            tf_password.setVisible(false);
            pf_password.setVisible(true);
            tf_password.clear();
        }
    }

    @FXML
    void loginAction(MouseEvent event) throws SQLException, IOException {
        externalLoginAction();
    }

    @FXML
    void keyLoginAction(KeyEvent event) throws SQLException, IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            externalLoginAction();
        }
    }

    private void externalLoginAction() throws SQLException, IOException {
        DBConnect db = new DBConnect();
        Connection conn = db.getCon();
        //dostęp do aktualnego okna
       Stage currentStage =(Stage) btn_login.getScene().getWindow();
        if (cb_show.isSelected()) {
            password = tf_password.getText();
        } else {
            password = pf_password.getText();
        }
        //przygotowanie zapytania do logowania
        PreparedStatement ps = conn.prepareStatement("SELECT permission, id_u FROM users where login=? AND password=?");
        ps.setString(1, tf_login.getText());
        ps.setString(2, password);

        ResultSet result = ps.executeQuery();

        int permission = -1;

        if (result.next()) {
            System.out.println("Zalogowano");
            permission = result.getInt("permission");
            id_u = result.getInt("id_u");
        }

        if (permission == 1) {
            System.out.println("Panel administratora");
            LoginCounter.counter = 2;

            currentStage.close();
        } else if (permission == 0) {
            LoginCounter.counter = 2;
            Parent root = FXMLLoader.load(getClass().getResource("/view/userView.fxml"));
            Scene scene = new Scene(root);
            Stage userStage = new Stage();
            userStage.setScene(scene);
            userStage.setTitle("Panel 1");
            Image icon = new Image(getClass().getResourceAsStream("../favicon.png"));
            userStage.getIcons().add(icon);
            userStage.show();
            currentStage.close();
        } else {
            LoginCounter.countAction();
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "Błąd",
                    "Wystąpił błąd logowania.",
                    "Podano błędne dane. Spróbuj ponownie.");
        }
    }
}
