package model;

public class Submission {
    private String login;
    private String course_name;
    private String date;
    private int confirm;

    public Submission(String login, String course_name, String date, int confirm) {
        this.login = login;
        this.course_name = course_name;
        this.date = date;
        this.confirm = confirm;
    }

    public Submission() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getConfirm() {
        return confirm;
    }

    public void setConfirm(int confirm) {
        this.confirm = confirm;
    }
}
