import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        System.out.println("Inicjalizuję okno aplikacji");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/loginView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Panel logowania");
        Image icon = new Image(getClass().getResourceAsStream("favicon.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Zamykam aplikację!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
