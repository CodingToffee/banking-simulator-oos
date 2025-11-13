package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class GeneralController {

    public void switchView(String targetview, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                .getResource(targetview));
        Parent root = (Parent) fxmlLoader.load();
        stage.setScene(new Scene(root));
    }

    public FXMLLoader loader(String targetview) {
        return new FXMLLoader(getClass().getClassLoader()
                .getResource(targetview));
    }

    public void ExceptionDialog(Exception e) {
        //TODO: dafür sorgen, dass ganzer Exception Text lesbar ist
        Alert exceptionAlert = new Alert(Alert.AlertType.ERROR);
        exceptionAlert.setTitle("Exception Dialog");
        exceptionAlert.setHeaderText("An Error has occurred.");
        exceptionAlert.setContentText(e.getMessage());
        System.out.println(e.getMessage());
        exceptionAlert.show();
    }

    public boolean deleteFromListView(ListView listView) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Bestätigen");
        alert.setHeaderText("Wollen Sie dieses Element wirklich löschen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            listView.getItems().remove(listView.getSelectionModel().getSelectedItem());
            return true;
        }
        else {
            return false;
        }
    }
}
