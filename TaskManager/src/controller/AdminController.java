package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Course;
import model.Submission;
import model.User;
import service.DBConnect;
import service.DialogWindow;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminController {

    @FXML
    private MenuItem m_logout;

    @FXML
    private MenuItem m_instruction;

    @FXML
    private MenuItem m_authors;

    @FXML
    private TableView<User> tbl_users;

    @FXML
    private TableColumn<User, String> col_users_name;

    @FXML
    private TableColumn<User, String> col_users_lastname;

    @FXML
    private TableColumn<User, String> col_users_login;

    @FXML
    private TableColumn<User, String> col_users_password;

    @FXML
    private Button btn_delete_user;

    @FXML
    private Button btn_change_password;

    @FXML
    private TextField tf_new_password1;

    @FXML
    private TextField tf_new_password2;

    @FXML
    private Button btn_confirm_new_password;

    @FXML
    private TableView<Course> tbl_course;

    @FXML
    private TableColumn<Course, String> col_course_name;

    @FXML
    private TableColumn<Course, String> col_course_agenda;

    @FXML
    private TableColumn<Course, String> col_course_category;

    @FXML
    private TableColumn<Course, String> col_course_date;

    @FXML
    private Button btn_insert_course;

    @FXML
    private TextArea ta_course_agenda;

    @FXML
    private TextField tf_course_name;

    @FXML
    private TextField tf_course_category;

    @FXML
    private DatePicker dp_course_date;
    @FXML
    private TableView<Submission> tbl_submission;

    @FXML
    private TableColumn<Submission, String> col_sub_login;

    @FXML
    private TableColumn<Submission, String> col_sub_course;

    @FXML
    private TableColumn<Submission, String> col_sub_date;

    @FXML
    private TableColumn<Submission, Integer> col_sub_confirm;

    @FXML
    private Button btn_confirm_submission;
    // obiekty globalne
    private DBConnect db;
    private PreparedStatement ps;
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Submission> submissionList = FXCollections.observableArrayList();

    @FXML
    void changePasswordAction(MouseEvent event) {
        btn_confirm_new_password.setVisible(true);
        tf_new_password1.setVisible(true);
        tf_new_password2.setVisible(true);
        btn_change_password.setVisible(false);
    }

    @FXML
    void confirmPasswordAction(MouseEvent event) throws SQLException {
        if (tf_new_password1.getText().equals(tf_new_password2.getText())) {
            try {
                db = new DBConnect();
                Connection conn = db.getCon();
                ps = conn.prepareStatement("UPDATE users SET password = ? WHERE login=?");
                ps.setString(1, tf_new_password2.getText());
                ps.setString(2, tbl_users.getSelectionModel().getSelectedItem().getLogin());
                ps.executeUpdate();
                globalSelect();
            } catch (NullPointerException e) {
                DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                        "BŁĄD",
                        "Błąd zmiany hasła",
                        "Musisz wybrać uzytkownika do zmiany hasła");
            }
        } else {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "BŁĄD",
                    "Podane hasło nie jest jednakowe w obu polach",
                    "Spróbuj ponownie.");
        }
        tf_new_password1.clear();
        tf_new_password2.clear();
        btn_confirm_new_password.setVisible(false);
        tf_new_password1.setVisible(false);
        tf_new_password2.setVisible(false);
        btn_change_password.setVisible(true);

    }

    @FXML
    void deleteUserAction(MouseEvent event) throws SQLException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("POTWIERDZENIE");
            alert.setHeaderText("Wymagane jest potwierdzenie operacji");
            alert.setContentText("Czy na pewno chcesz usunąć tego użytkownika?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                db = new DBConnect();
                Connection conn = db.getCon();
                ps = conn.prepareStatement("DELETE FROM users WHERE login=?");
                ps.setString(1, tbl_users.getSelectionModel().getSelectedItem().getLogin());
                ps.executeUpdate();
                globalSelect();
            }
        } catch (NullPointerException e) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "BŁĄD",
                    "Błąd usuwania użytkownika",
                    "Musisz wybrać uzytkownika do usunięcia");
        }
    }

    @FXML
    void menuAuthors(ActionEvent event) {
        DialogWindow dw = new DialogWindow(Alert.AlertType.INFORMATION,
                "Instrukcja",
                "Instrukcja korzystania z panelu administratora",
                "Tu będą kolejne instrukcje:");
    }

    @FXML
    void menuInstruction(ActionEvent event) {

        DialogWindow dw = new DialogWindow(Alert.AlertType.INFORMATION,
                "O autorach",
                "Info o autorach",
                "Kursanci Reaktor PWN");
    }

    @FXML
    void menuLogout(ActionEvent event) throws IOException {
        Stage currentStage = (Stage) btn_delete_user.getScene().getWindow();
        currentStage.close();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Panel logowania");
        Image icon = new Image(getClass().getResourceAsStream("..//favicon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();

    }

    private void globalSelect() throws SQLException {
        userList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("SELECT name, lastname, login, password FROM users");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            User user = new User(result.getString("name"),
                    result.getString("lastname"),
                    result.getString("login"),
                    result.getString("password"));
            userList.add(user);
        }
        col_users_name.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col_users_lastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
        col_users_login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        col_users_password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        tbl_users.setItems(userList);

    }
    //tab2 - kursy

    @FXML
    void addCourseAction(MouseEvent event) throws SQLException {

        if (tf_course_name.getText().trim().equals("")) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR, "BŁĄD", "Błąd dodawania kursu", "Podaj nazwę kursu");
        } else if (tf_course_category.getText().trim().equals("")) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR, "BŁĄD", "Błąd dodawania kursu", "Podaj kategorię kursu");
        } else if (ta_course_agenda.getText().trim().equals("")) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR, "BŁĄD", "Błąd dodawania kursu", "Podaj agendę kursu");
        } else if (dp_course_date.getValue() == null) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR, "BŁĄD", "Błąd dodawania kursu", "Wybierz datę kursu");
        } else {
            db = new DBConnect();
            Connection conn = db.getCon();
            ps = conn.prepareStatement("INSERT INTO course (course_name,course_agenda,course_category) values(?,?,?)");
            ps.setString(1, tf_course_name.getText());
            ps.setString(2, ta_course_agenda.getText());
            ps.setString(3, tf_course_category.getText());
            ps.executeUpdate();
            ps = conn.prepareStatement("INSERT INTO edition (date,course_id_c) values(?," +
                    "(select id_c from course where course_name=?))");
            ps.setString(1, dp_course_date.getValue().toString());
            ps.setString(2, tf_course_name.getText());
            ps.executeUpdate();
            globalCourseSelect();
            tf_course_category.clear();
            tf_course_name.clear();
            ta_course_agenda.clear();
            dp_course_date.getEditor().clear();
        }


    }

    private void globalCourseSelect() throws SQLException {
        courseList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("select course_name, " +
                "course_agenda," +
                "course_category, " +
                "date from course join edition on (id_c=edition.course_id_c);");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            Course course = new Course(result.getString("course_name"),
                    result.getString("course_agenda"),
                    result.getString("course_category"),
                    result.getString("date"));
            courseList.add(course);
        }
        col_course_name.setCellValueFactory(new PropertyValueFactory<Course, String>("course_name"));
        col_course_agenda.setCellValueFactory(new PropertyValueFactory<Course, String>("course_agenda"));
        col_course_category.setCellValueFactory(new PropertyValueFactory<Course, String>("course_category"));
        col_course_date.setCellValueFactory(new PropertyValueFactory<Course, String>("date"));
        tbl_course.setItems(courseList);

    }
    //tab3 - zapisy

    @FXML
    void confirmSubmissionAction(MouseEvent event) throws SQLException {
        int status = tbl_submission.getSelectionModel().getSelectedItem().getConfirm();
        try {
            if (status == 0) {
                status = 1;
            } else {
                status = 0;
            }
            db = new DBConnect();
            Connection conn = db.getCon();
            ps = conn.prepareStatement("UPDATE submission SET confirm = ? WHERE (\n" +
                    "users_id_u=(select id_u from users where login =?)\n" +
                    " and\n" +
                    " course_id_c=(select id_c from course full join edition on (id_c = course_id_c) where course_name =? and date=?)\n" +
                    " and date = (select date from course full join edition on (id_c = course_id_c) where course_name =? and date=?)\n" +
                    " );");
            ps.setInt(1, status);
            ps.setString(2, tbl_submission.getSelectionModel().getSelectedItem().getLogin());
            ps.setString(3, tbl_submission.getSelectionModel().getSelectedItem().getCourse_name());
            ps.setString(4, tbl_submission.getSelectionModel().getSelectedItem().getDate());
            ps.setString(5, tbl_submission.getSelectionModel().getSelectedItem().getCourse_name());
            ps.setString(6, tbl_submission.getSelectionModel().getSelectedItem().getDate());
            ps.executeUpdate();
            globalSubmissionSelect();
        } catch (NullPointerException e) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "BŁĄD",
                    "Błąd danych",
                    "Musisz wybrak rekord do zmiany statusu!");
        }
    }

    @FXML
    void btnActivation(MouseEvent event) {
        try {
            if (tbl_submission.getSelectionModel().getSelectedItem().getConfirm() == 0) {
                btn_confirm_submission.setVisible(true);
                btn_confirm_submission.setStyle("-fx-background-color:green");
                btn_confirm_submission.setText("potwierdź");
            } else if (tbl_submission.getSelectionModel().getSelectedItem().getConfirm() == 1) {
                btn_confirm_submission.setVisible(true);
                btn_confirm_submission.setStyle("-fx-background-color:red");
                btn_confirm_submission.setText("odrzuć");
            } else {
                btn_confirm_submission.setVisible(false);
            }
        } catch (NullPointerException e) {
            DialogWindow dw = new DialogWindow(Alert.AlertType.ERROR,
                    "BŁĄD",
                    "Błąd danych",
                    "Musisz wybrak rekord do zmiany statusu!");
        }

    }


    private void globalSubmissionSelect() throws SQLException {
        submissionList.clear();
        db = new DBConnect();
        Connection conn = db.getCon();
        ps = conn.prepareStatement("select login, course_name, date, confirm \n" +
                "\tfrom users join submission on (id_u=users_id_u)\n" +
                "\t\t\t\tjoin course on (id_c = submission.course_id_c)\n" +
                "                join edition on (id_c = edition.course_id_c)\n" +
                "\torder by confirm asc;");
        ResultSet result = ps.executeQuery();
        while (result.next()) {
            Submission course = new Submission(result.getString("login"),
                    result.getString("course_name"),
                    result.getString("date"),
                    result.getInt("confirm"));
            submissionList.add(course);
        }
        col_sub_login.setCellValueFactory(new PropertyValueFactory<Submission, String>("login"));
        col_sub_course.setCellValueFactory(new PropertyValueFactory<Submission, String>("course_name"));
        col_sub_date.setCellValueFactory(new PropertyValueFactory<Submission, String>("date"));
        col_sub_confirm.setCellValueFactory(new PropertyValueFactory<Submission, Integer>("confirm"));
        tbl_submission.setItems(submissionList);

    }

    public void initialize() throws SQLException {
        globalSelect();
        globalCourseSelect();
        globalSubmissionSelect();
    }

}
