package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FxApplication extends javafx.application.Application{
    public static void main(String args[]) { launch(args);}

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                    .getResource("mainview.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Praktikum 5");
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}
