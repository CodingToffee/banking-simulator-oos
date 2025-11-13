package ui;

import bank.*;
import bank.exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.stage.Stage;

public class MainviewController extends GeneralController implements Initializable {
    @FXML
    private javafx.scene.control.ListView<String> myListView;
    @FXML
    private MenuItem MIselect;
    @FXML
    private MenuItem MIdelete;
    @FXML
    private ContextMenu contextMenu;

    private
    String directoryName = "D:\\Eigene Dateien\\Dokumente\\UNI\\OOS\\Accounts2";
    PrivateBank myBank = SingleBank.getInstance();
    /**
     * Initializes the Bank with Dummy values
     * @throws IOException
     * @throws AccountAlreadyExistsException
     * @throws AccountDoesNotExistException
     * @throws TransactionAlreadyExistException
     * @throws TransactionAttributeException
     * @throws TransactionInvalidAttributException
     */
    public void initAccounts() throws IOException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAlreadyExistException, TransactionAttributeException, TransactionInvalidAttributException, InterruptedException {

        Payment paymentNico = new Payment("22.12.2022",100,"Initial deposit",0.1,0.2);
        OutgoingTransfer outgoingTransferNico = new OutgoingTransfer("23.12.2022",50,"Christmas gift","Nicolas","Christina");
        IncomingTransfer incomingTransferNico = new IncomingTransfer("24.12.2022",60.66,"Christma$ monney","pimp","Nicolas");
        Payment paymentIna = new Payment("22.12.2022",100,"Initial deposit",0.1,0.2);
        OutgoingTransfer outgoingTransferIna = new OutgoingTransfer("23.12.2022",50,"Christmas gift","Christina","Nicolas");
        IncomingTransfer incomingTransferIna = new IncomingTransfer("24.12.2022",60.66,"Christma$ monney","Judith","Christina");
        Payment paymentJudith = new Payment("22.12.2022",100,"Initial deposit",0.1,0.2);
        OutgoingTransfer outgoingTransferJudith = new OutgoingTransfer("23.12.2022",50,"Christmas gift","Judith","Christina");
        IncomingTransfer incomingTransferJudith = new IncomingTransfer("24.12.2022",60.66,"Christma$ monney","Nicolas","Judith");

        //Accounts clearen um Fehler zu vermeiden
        File file0 = new File(directoryName + "\\Konto Nicolas.json");
        file0.delete();
        file0 = new File(directoryName + "\\Konto Christina.json");
        file0.delete();
        file0 = new File(directoryName + "\\Konto Judith.json");
        file0.delete();
        myBank.createAccount("Nicolas");
        myBank.addTransaction("Nicolas",paymentNico);
        myBank.addTransaction("Nicolas",outgoingTransferNico);
        myBank.addTransaction("Nicolas",incomingTransferNico);
        System.out.println("Account Nicolas angelegt");
        myBank.createAccount("Christina");
        myBank.addTransaction("Christina",paymentIna);
        myBank.addTransaction("Christina",outgoingTransferIna);
        myBank.addTransaction("Christina",incomingTransferIna);
        System.out.println("Account Christina angelegt");
        myBank.createAccount("Judith");
        myBank.addTransaction("Judith",paymentJudith);
        myBank.addTransaction("Judith",outgoingTransferJudith);
        myBank.addTransaction("Judith",incomingTransferJudith);
        System.out.println("Account Judith angelegt");
    }

    /**
     * Initializes the ListView field
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
/*
        try {
            initAccounts();
        } catch (Exception e) {
            ExceptionDialog(e);
        }
*/

        myListView.getItems().addAll(myBank.getAllAccounts());
    }

    /**
     * Switches the Stage to the AccountView of the selected Account
     * @param actionEvent
     * @throws IOException
     */
    public void selectAction(javafx.event.ActionEvent actionEvent) throws IOException, AccountDoesNotExistException {
        Stage stage = (Stage)myListView.getScene().getWindow();
        String selectedAccount = myListView.getSelectionModel().getSelectedItem();
        SingleBank.setAccountSelected(selectedAccount);
        switchView("accountview.fxml",stage);
    }

    /**
     * Deletes the selected Account
     * @param actionEvent
     */
    public void deleteAction(ActionEvent actionEvent) {
        Object selectedItem = myListView.getSelectionModel().getSelectedItem();
        if (deleteFromListView(myListView)){
            try {
                myBank.deleteAccount(selectedItem.toString());
            }
            catch (Exception e) {
                ExceptionDialog(e);
            }
            //System.out.println(myBank.getAllAccounts());
         }
    }

    public void newAccountButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                .getResource("newaccountview.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Add New Account");
        stage.showAndWait();
        myListView.getItems().clear();
        myListView.getItems().addAll(myBank.getAllAccounts());
    }
}
