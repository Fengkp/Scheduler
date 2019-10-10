import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DatabaseConnection;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println(Timestamp.valueOf(LocalDateTime.now()));
        DatabaseConnection.getInstance().connect();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/LoginView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Login");
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
