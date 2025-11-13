package bank;


import bank.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrivateBankTest {

    String directoryLaptop = "D:\\Eigene Dateien\\Dokumente\\UNI\\OOS\\";
    String directoryPC = "F:\\Users\\nicol\\Documents\\UNI\\OOS\\P4\\";
    String directory = directoryLaptop;
    String name0 = "privateBank0";
    String name1 = "privateBank1";
    double incomingInterest0 = 0.01;
    double incomingInterest1 = 0.02;
    double outgoingInterest0 = 0.03;
    double outgoingInterest1 = 0.04;
    String directoryName0 = directory + "Accounts0";
    String directoryName1 = directory + "Accounts1";
    String directoryName2 = directory + "Accounts2";

    //Attribute Payment
    int amount_payment0 = 400;
    int amount_payment1 = 500;
    double amount_payment2 = -333.33;
    String date_payment0 = "09.12.2022";
    String date_payment1 = "09.12.2023";
    String date_payment2 = "12.10.2020";
    String description_payment0 = "Test0_payment";
    String description_payment1 = "Test1_payment";
    String description_payment2 = "Test2_payment";
    double incomingInterest_payment0 = 0.1;
    double incomingInterest_payment1 = 0.2;
    double incomingInterest_payment2 = 0.3;
    double outgoingInterest_payment0 = 0.15;
    double outgoingInterest_payment1 = 0.25;
    double outgoingInterest_payment2 = 0.23;

    //Attribute Transfer
    int amount_transfer0 = 400;
    int amount_transfer1 = 500;
    String date_transfer0 = "12.12.2022";
    String date_transfer1 = "22.11.2023";
    String description_incTransfer0 = "Test0_incTransfer";
    String description_outTransfer0 = "Test1_outTransfer";
    String sender_transfer0 = "sender0";
    String sender_transfer1 = "sender1";
    String recipient_transfer0 = "account0";
    String recipient_transfer1 = "account1";


    //PrivateBanks
    PrivateBank privateBank0 = null;
    PrivateBank privateBank1 = null;
    PrivateBank privateBank2 = null;
    //Accounts
    String account0 = "account0";
    String account1 = "account1";
    //Transfers
    IncomingTransfer incTransfer0 = null;
    OutgoingTransfer outTransfer0 = null;
    OutgoingTransfer outgoingTransfer = null;
    Payment payment0 = null;
    Payment payment1 = null;
    Payment payment2 = null;
    //TransactionList
    List<Transaction> transactionList0 = new ArrayList<>();
    List<Transaction> transactionList1 = new ArrayList<>();
    List<Transaction> transactionList_neg = new ArrayList<>();

    @BeforeEach
    void init() throws IOException, AccountAlreadyExistsException, TransactionInvalidAttributException, TransactionAlreadyExistException, TransactionAttributeException, AccountDoesNotExistException{
        privateBank0 = new PrivateBank(name0, incomingInterest0, outgoingInterest0, directoryName0);
        privateBank1 = new PrivateBank(name1, incomingInterest1, outgoingInterest1, directoryName1);
        privateBank0.createAccount(account0);
        incTransfer0 = new IncomingTransfer(date_transfer0,amount_transfer0, description_incTransfer0, sender_transfer0, recipient_transfer0);
        outTransfer0 = new OutgoingTransfer(date_transfer1,amount_transfer1, description_outTransfer0, sender_transfer1, recipient_transfer1);
        payment0 = new Payment(date_payment0,amount_payment0, description_payment0, incomingInterest_payment0, outgoingInterest_payment0);
        payment1 = new Payment(date_payment1,amount_payment1, description_payment1, incomingInterest_payment1, outgoingInterest_payment1);
        payment2 = new Payment(date_payment2,amount_payment2,description_payment2,incomingInterest_payment2,outgoingInterest_payment2);
        outgoingTransfer = new OutgoingTransfer(date_transfer0,amount_transfer0, description_incTransfer0,sender_transfer0,recipient_transfer0);
        transactionList1.add(incTransfer0);
        transactionList1.add(outTransfer0);
        transactionList1.add(payment0);
        transactionList1.add(payment1);
        transactionList1.add(payment2);
        transactionList1.add(outgoingTransfer);
        privateBank1.createAccount(account1, transactionList1);
        privateBank0.addTransaction(account0, incTransfer0);
        privateBank0.addTransaction(account0,payment0);
        transactionList0.add(incTransfer0);
        transactionList0.add(payment0);
        transactionList_neg.add(outTransfer0);
        transactionList_neg.add(payment2);
        transactionList_neg.add(outgoingTransfer);


        privateBank2 = new PrivateBank(privateBank0);
    }



    @AfterEach
    void deletePersistentData() throws IOException, InterruptedException {
        File file0 = new File(directoryName0 + "\\Konto account0.json");
        file0.delete();
        File file1 = new File(directoryName1 + "\\Konto account1.json");
        file1.delete();
        File file1_0 = new File(directoryName1 + "\\Konto account0.json");
        file1_0.delete();
        File file2 = new File(directoryName2 + "\\Konto account2.json");
        file2.delete();
        File file0_1 = new File(directoryName0 + "\\Konto account0_1.json");
        file0_1.delete();
        File file0_2 = new File(directoryName0 + "\\Konto account0_2.json");
        file0_2.delete();

    }


    @Test
    @DisplayName("Konstruktor")
    void constructor() {
        assertNotNull(privateBank0);
        assertEquals(name0, privateBank0.getName());
        assertEquals(incomingInterest0, privateBank0.getIncomingInterest());
        assertEquals(outgoingInterest0, privateBank0.getOutgoingInterest());
        assertEquals(directoryName0, privateBank0.getDirectoryName());
    }


    @Test
    @DisplayName("Copy Konstruktor")
    void copyConstructor() throws IOException {
        assertNotNull(privateBank2);
        assertEquals(privateBank0.getName(), privateBank2.getName());
        assertEquals(privateBank0.getIncomingInterest(), privateBank2.getIncomingInterest());
        assertEquals(privateBank0.getOutgoingInterest(), privateBank2.getOutgoingInterest());
        assertEquals(privateBank0.getDirectoryName(), privateBank2.getDirectoryName());
        //assertnotsame
    }

    @Test
    void testToString() {
        assertEquals("PrivateBank{" +
                "Name='" + name0 + '\'' +
                ", IncomingInterest='" + incomingInterest0 + '\'' +
                ", OutgoingInterest='" + outgoingInterest0 + '\'' +
                ", DirectoryName='" + directoryName0 + '\'' +
                ", AccountsToTransactions='" + privateBank0.accountsToTransactions + '\'' +
                '}', privateBank0.toString());
    }

    @Test
    void testEquals() {
        assertEquals(privateBank0, privateBank2);
        assertNotEquals(privateBank0, privateBank1);
        assertNotEquals(null, privateBank0);
        //andere datentypen test
    }

    @Test
    void createAccount() {
        assertTrue(privateBank0.accountsToTransactions.containsKey(account0));
        assertThrows(AccountAlreadyExistsException.class,() -> privateBank0
                .createAccount(account0));
    }

    @Test
    void createAccountTransactions() throws IOException, AccountDoesNotExistException {

        assertTrue(privateBank1.accountsToTransactions.containsKey(account1));
        assertEquals(transactionList1,privateBank1.getTransactions(account1));
        assertThrows(AccountAlreadyExistsException.class,() -> privateBank1
                .createAccount(account1, transactionList1));
        transactionList1.clear();
        transactionList1.add(incTransfer0);
        transactionList1.add(incTransfer0);
        assertThrows(TransactionAlreadyExistException.class,() -> privateBank1
                .createAccount(account0, transactionList1));
    }

    @Test
    void addTransaction() throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(incTransfer0));
        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(payment0));
        assertThrows(TransactionAlreadyExistException.class,() -> privateBank0
                .addTransaction(account0, incTransfer0));
        //Wird bei mir nie geworfen, da sie schon im Konstruktor der Transaction abgefangen wird
       // assertThrows(TransactionAttributeException.class,() -> privateBank0
         //       .addTransaction(account0,payment0));
        assertThrows(AccountDoesNotExistException.class,() -> privateBank0
                .addTransaction("doesntExist", outTransfer0));


    }

    @Test
    void removeTransaction() {

        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(incTransfer0));
        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(payment0));
        assertDoesNotThrow(() -> privateBank0.removeTransaction(account0, incTransfer0));
        assertFalse(privateBank0.accountsToTransactions.get(account0).contains(incTransfer0));
        assertThrows(AccountDoesNotExistException.class,() -> privateBank0
                .removeTransaction("doesntExist", outTransfer0));


    }


    @Test
    void containsTransaction() {
        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(incTransfer0));
        assertTrue(privateBank0.accountsToTransactions.get(account0).contains(payment0));
        assertFalse(privateBank0.accountsToTransactions.get(account0).contains(outTransfer0));
        assertFalse(privateBank0.accountsToTransactions.get(account0).contains(payment1));
        assertFalse(privateBank0.accountsToTransactions.get(account0).contains(null));
    }

    @Test
    void getAccountBalance() throws AccountDoesNotExistException {
        double accountBalance = incTransfer0.calculate() + outTransfer0.calculate()
                                + payment0.calculate() + payment1.calculate()
                                + payment2.calculate() + outgoingTransfer.calculate();
        assertEquals(accountBalance,privateBank1.getAccountBalance(account1));
        accountBalance = incTransfer0.calculate() + payment0.calculate();
        assertEquals(accountBalance,privateBank0.getAccountBalance(account0));
    }


    @Test
    void getTransactions() throws IOException, AccountDoesNotExistException {
        assertEquals(transactionList1,privateBank1.getTransactions(account1));
        assertEquals(transactionList0,privateBank0.getTransactions(account0));
    }

    @Test
    void getTransactionsSorted() throws IOException, AccountDoesNotExistException {
        transactionList1.clear();
        //incTransfer0: 400; outTransfer0: -500; outgoingTransfer: -400; payment0: 396; payment1: 490; payment2: -346.66
        //Aufsteigende Sortierung
        transactionList1.add(outTransfer0);
        transactionList1.add(outgoingTransfer);
        transactionList1.add(payment2);
        transactionList1.add(payment0);
        transactionList1.add(incTransfer0);
        transactionList1.add(payment1);
        assertEquals(transactionList1, privateBank1.getTransactionsSorted(account1,true));
        //Absteigende Sortierung
        transactionList1.clear();
        transactionList1.add(payment1);
        transactionList1.add(incTransfer0);
        transactionList1.add(payment0);
        transactionList1.add(payment2);
        transactionList1.add(outgoingTransfer);
        transactionList1.add(outTransfer0);
        assertEquals(transactionList1, privateBank1.getTransactionsSorted(account1,false));
    }

    @Test
    void getTransactionsByType() throws IOException, AccountDoesNotExistException {
        //Positive Transaktionen, negative aus Liste entfernen
        transactionList1.remove(4);
        transactionList1.remove(4);
        transactionList1.remove(1);
        assertEquals(transactionList1,privateBank1.getTransactionsByType(account1,true));
        assertEquals(transactionList_neg,privateBank1.getTransactionsByType(account1,false));
    }

    @Test
    void deleteAccount() {
        assertDoesNotThrow(() -> privateBank0.deleteAccount(account0));
        assertThrows(AccountDoesNotExistException.class,() -> privateBank0.getAccountBalance(account0));
        assertDoesNotThrow(() -> privateBank1.deleteAccount(account1));
        assertThrows(AccountDoesNotExistException.class,() -> privateBank1.getAccountBalance(account1));
    }

    @Test
    void getAccounts() {
        assertDoesNotThrow(() -> privateBank0.createAccount("account0_1"));
        assertDoesNotThrow(() -> privateBank0.createAccount("account0_2"));
        List<String> accountList = new ArrayList<>();
        accountList.add("account0_1");
        accountList.add("account0_2");
        accountList.add("account0");
        assertEquals(accountList,privateBank0.getAllAccounts());
    }

}

