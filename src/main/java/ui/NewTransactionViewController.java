package ui;

import bank.OutgoingTransfer;
import bank.Payment;
import bank.SingleBank;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewTransactionViewController extends AccountviewController {
    @FXML
    private SplitMenuButton splitMenu;
    @FXML
    private MenuItem payment;
    @FXML
    private MenuItem transfer;
    @FXML
    private Label date;
    @FXML
    private Label amount;
    @FXML
    private Label description;
    @FXML
    private Label sender;
    @FXML
    private Label receiver;
    @FXML
    private TextField transactionDate;
    @FXML
    private TextField transactionAmount;
    @FXML
    private TextField transactionDescription;
    @FXML
    private TextField transactionSender;
    @FXML
    private TextField transactionReceiver;
    @FXML
    private Button createTransaction;

    public void paymentAction(ActionEvent actionEvent) {
        //hide sender and receiver
        sender.setVisible(false);
        receiver.setVisible(false);
        transactionReceiver.setVisible(false);
        transactionSender.setVisible(false);
        splitMenu.setText("Payment");
    }

    public void transferAction(ActionEvent actionEvent) {
        sender.setVisible(true);
        receiver.setVisible(true);
        transactionReceiver.setVisible(true);
        transactionSender.setVisible(true);
        splitMenu.setText("Transfer");
    }

    public void createTransactionAction(ActionEvent actionEvent) {
        //TODO: Fallunterscheidung Payment und Transfer
        if (transactionDate.getText().isEmpty() ||
            transactionAmount.getText().isEmpty() ||
            transactionDescription.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Incomplete Data");
                alert.setHeaderText("You got to fill out all the fields");
                alert.show();

        } else if ((transactionSender.getText().isEmpty() ||
                transactionReceiver.getText().isEmpty()) && splitMenu.getText().equals("Transfer")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incomplete Data");
            alert.setHeaderText("You got to fill out all the fields");
            alert.show();
        }
        else {
            //Überprüfe, welche Transaction vorliegt
            if (splitMenu.getText().equals("Payment")) {
                //Erstelle Payment
                Payment payment = new Payment(transactionDate.getText(), Double.parseDouble(transactionAmount.getText()), transactionDescription.getText());
                //Füge Payment zu Account hinzu
                try {
                    SingleBank.getInstance().addTransaction(SingleBank.getAccountSelected(), payment);
                }
                catch (Exception e) {
                    ExceptionDialog(e);
                }
            }
            else {
                //Erstelle Transfer
                //Überprüfe, ob IncomingTransfer oder OutgoingTransfer -> wenn negativer Betrag, dann OutgoingTransfer
                if (Double.parseDouble(transactionAmount.getText()) < 0) {
                    //Erstelle OutgoingTransfer
                    try {
                        OutgoingTransfer transfer = new OutgoingTransfer(transactionDate.getText(), Double.parseDouble(transactionAmount.getText()), transactionDescription.getText(), transactionSender.getText(), transactionReceiver.getText());
                        //Füge Transfer zu Account hinzu
                        SingleBank.getInstance().addTransaction(SingleBank.getAccountSelected(), transfer);
                    } catch (Exception e) {
                        ExceptionDialog(e);
                    }
                }
                else {
                    //Erstelle IncomingTransfer
                    try {
                        bank.IncomingTransfer transfer = new bank.IncomingTransfer(transactionDate.getText(), Double.parseDouble(transactionAmount.getText()), transactionDescription.getText(), transactionSender.getText(), transactionReceiver.getText());
                        //Füge Transfer zu Account hinzu
                        SingleBank.getInstance().addTransaction(SingleBank.getAccountSelected(), transfer);
                    } catch (Exception e) {
                        ExceptionDialog(e);
                    }
                }
            }
        }
        //Schließe NewTransactionView
        Stage stage = (Stage) splitMenu.getScene().getWindow();
        stage.close();

    }
}
