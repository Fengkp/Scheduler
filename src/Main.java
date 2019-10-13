import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DatabaseConnection;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ZonedDateTime time1 = LocalDateTime.now().atZone(ZoneId.systemDefault());
        System.out.println(time1);
        ZonedDateTime time2 = time1.withZoneSameInstant(ZoneId.of("UTC"));
        System.out.println(time2);

        ZonedDateTime time3 = time2.toLocalDateTime().atZone(ZoneId.of("UTC"));
        System.out.println(time3);
        ZonedDateTime time4 = time3.withZoneSameInstant(ZoneId.systemDefault());
        System.out.println(time4);


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
