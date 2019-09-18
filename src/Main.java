import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection.getInstance().connect();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/LoginView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
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