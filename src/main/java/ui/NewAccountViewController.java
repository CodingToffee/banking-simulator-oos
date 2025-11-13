package ui;

import bank.SingleBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewAccountViewController extends GeneralController{
    @FXML
    private Button createAccount;
    @FXML
    private TextField nameField;
    public void createAccountAction(ActionEvent actionEvent) {
        try {
            SingleBank.getInstance().createAccount(nameField.getText());
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        Stage stage = (Stage) createAccount.getScene().getWindow();
        stage.close();
    }
}
