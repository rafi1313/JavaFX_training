package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.DBConnect;
import service.DialogWindow;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;

public class UserController {

    @FXML
    private Label lbl_header;

    @FXML
    private ComboBox<String> combo_category;

    @FXML
    private ComboBox<String> combo_date;

    @FXML
    private ComboBox<String> combo_name;

    @FXML
    private TextArea ta_agenda;

    @FXML
    private RadioButton rb_normal;

    @FXML
    private ToggleGroup tg_feed;

    @FXML
    private RadioButton rb_vege;

    @FXML
    private RadioButton rb_gluten;

    @FXML
    private CheckBox cb_fv;

    @FXML
    private TextArea ta_fv;

    @FXML
    private Button btn_submit;
    // zmienne globalne
    private String category = "%";
    private String date = "%";

    // obiekty globalne
    private DBConnect db;
    private PreparedStatement ps;
    private ObservableList<String> course_names = FXCollections.observableArrayList();
    private ObservableList<String> category_names = FXCollections.observableArrayList();
    private ObservableList<String> dates = FXCollections.observableArrayList();

    @FXML
    void categoryFilter(ActionEvent event) throws SQLException {
        category = combo_category.getValue();
        externalFilter(category, date);
    }

    @FXML
    void dateFilter(ActionEvent event) throws SQLException {
        date = combo_date.getValue();
        externalFilter(category, date);
    }

    private void externalFilter(String category, String date) throws SQLException {
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT DISTINCT course_name FROM course join edition on (course_id_c=id_c) where course_category like ? AND date LIKE ?");
        ps.setString(1, category);
        ps.setString(2, date);
        ResultSet result = ps.executeQuery();
        course_names.clear();
        while (result.next()) {
            course_names.add(result.getString("course_name"));
        }
        combo_name.setItems(course_names);
    }

    @FXML
    void fvAction(MouseEvent event) {
        if (cb_fv.isSelected()) {
            ta_fv.setDisable(false);
        } else {
            ta_fv.clear();
            ta_fv.setDisable(true);
        }
    }

    @FXML
    void nameAction(ActionEvent event) throws SQLException {

        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT course_agenda FROM course WHERE course_name =?");
        ps.setString(1, combo_name.getValue());
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            ta_agenda.setText(result.getString("course_agenda"));
        }
    }

    @FXML
    void submitAction(MouseEvent event) throws SQLException, IOException {
//        System.out.println(combo_name.getValue());
        if (combo_name.getValue() == null) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "Błąd",
                    "Nie wybrano kursu",
                    "Wybierz kurs i spróbuj ponownie.");
        } else {
            String feed;
            if (rb_normal.isSelected()) {
                feed = "normalne";
            } else if (rb_vege.isSelected()) {
                feed = "wegetariańskie";
            } else {
                feed = "bezglutenowe";
            }
            String fv_decision = "0";

            if (cb_fv.isSelected()) {
                fv_decision = "1";
            }

            db = new DBConnect();
            Connection conn = db.getCon();
            ps = conn.prepareStatement("INSERT INTO submission (users_id_u,course_id_c,feed,fv_decision,fv_details) " +
                    "values (?," +
                    "(select id_c from course where course_name=?)," +
                    "?," +
                    "?," +
                    "?)");
            ps.setInt(1, LoginController.id_u);
            ps.setString(2, combo_name.getValue());
            ps.setString(3, feed);
            ps.setString(4, fv_decision);
            ps.setString(5, ta_fv.getText());
//            System.out.println(ps.toString());
            ps.executeUpdate();
//            System.out.println("END");
//            conn.commit();

            DialogWindow dw = new DialogWindow(Alert.AlertType.INFORMATION,
                    "Potwierdzenie",
                    "Dokonano zapisu!",
                    "Zaczekaj teraz na zatwierdzenie udziału.");

            //wygenerowanie zapisu do pliku
            String fileName = new SimpleDateFormat("yyyy-MM-dd_HH_mm'.txt'").format(new Timestamp(System.currentTimeMillis()));
            PrintWriter plik = new PrintWriter(new FileOutputStream("X:\\BeD kurs\\JavaFX\\JavaFX_training\\TaskManager\\src\\output\\file"+fileName));
            String zapis = new StringBuilder().append(LoginController.id_u).append(";").append(combo_name.getValue()).append(";").append(feed).append(";").append(fv_decision).append(";").append(ta_fv.getText()).append("\n").toString();
            plik.write(zapis);
            plik.close();
        }
    }

    public void initialize() throws SQLException {
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT course_name FROM course");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            course_names.add(result.getString("course_name"));
        }
        combo_name.setItems(course_names);

        //1. uzupełnienie kategorii w combobox
        ps = conn.prepareStatement("SELECT DISTINCT course_category FROM course");
        ResultSet result_category = ps.executeQuery();
        while (result_category.next()) {
            category_names.add(result_category.getString("course_category"));
        }
        combo_category.setItems(category_names);
        //2. uzupełnienie dat w combobox
        ps = conn.prepareStatement("SELECT DISTINCT date FROM edition");
        ResultSet result_dates = ps.executeQuery();
        while (result_dates.next()) {
            dates.add(result_dates.getString("date"));
        }
        combo_date.setItems(dates);
    }

}
