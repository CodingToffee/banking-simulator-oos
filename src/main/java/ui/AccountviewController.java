package ui;

import bank.PrivateBank;
import bank.SingleBank;
import bank.Transaction;
import bank.Transfer;
import bank.exceptions.AccountDoesNotExistException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountviewController extends GeneralController implements Initializable {

    @FXML
    public Text account = new Text();
    @FXML
    public Text accountBalance = new Text();
    @FXML
    private Button buttonBack;
    @FXML
    private MenuItem sortAscending;
    @FXML
    private MenuItem sortDescending;
    @FXML
    private MenuItem positiveTransactions;
    @FXML
    private MenuItem negativeTransactions;
    @FXML
    private MenuItem delete;
    @FXML
    public javafx.scene.control.ListView<Transaction> accountListView = new ListView<>();
    public ObservableList<Transaction> obList = FXCollections.observableArrayList();
    public PrivateBank myBank = SingleBank.getInstance();
    public String myAccount = SingleBank.getAccountSelected();


    public void setAccountBalance() {
        try {
            accountBalance.setText(SingleBank.getInstance().getAccountBalance(SingleBank.getAccountSelected()) + " €");
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        account.setText(SingleBank.getAccountSelected());
        try {
            obList.addAll(myBank.getTransactions(myAccount));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
        setAccountBalance();
    }

    public void deleteTransactionAction(ActionEvent actionEvent) throws AccountDoesNotExistException, IOException {
        Transaction selectedItem = accountListView.getSelectionModel().getSelectedItem();
        if (deleteFromListView(accountListView)) {
            try {
                myBank.removeTransaction(myAccount,selectedItem);
            } catch (Exception e) {
                ExceptionDialog(e);
            }
            setAccountBalance();
            System.out.println(myBank.getTransactions(myAccount));
        }
    }


    public void buttonBackAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage)accountListView.getScene().getWindow();
        switchView("mainview.fxml", stage);
    }


    public void sortAscendingAction(ActionEvent actionEvent) {
        obList.clear();
        try {
            obList.addAll(myBank.getTransactionsSorted(myAccount, true));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
    }

    public void sortDescendingAction(ActionEvent actionEvent) {
        obList.clear();
        try {
            obList.addAll(myBank.getTransactionsSorted(myAccount, false));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
    }

    public void positiveTransactionsAction(ActionEvent actionEvent) {
        obList.clear();
        try {
            obList.addAll(myBank.getTransactionsByType(myAccount,true));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
    }

    public void negativeTransactionsAction(ActionEvent actionEvent) {
        obList.clear();
        try {
            obList.addAll(myBank.getTransactionsByType(myAccount,false));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
    }

    public void addNewTransactionAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader()
                .getResource("newtransactionview.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("Add New Transaction");
        stage.showAndWait();
        //refresh listview
        obList.clear();
        try {
            obList.addAll(myBank.getTransactions(myAccount));
        }
        catch (Exception e) {
            ExceptionDialog(e);
        }
        accountListView.setItems(obList);
        setAccountBalance();
    }
}
