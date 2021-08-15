package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("My Dictionary");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.setOnCloseRequest(e->{
            DBDealer.getInstance().closeAll();
            System.out.println("System quits =============>");
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
