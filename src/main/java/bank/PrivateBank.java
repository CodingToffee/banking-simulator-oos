package bank;

import bank.exceptions.*;
import bank.debug.COLORS;
import com.google.gson.*;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class PrivateBank implements Bank {
    /**
     * Gibt den Namen der Bank an
     */
    String name;


    /**
     * Gibt den Zinssatz an, der bei einer Einzahlung anfällt
     * zwischen 0 und 1, positiv
     */
    private double IncomingInterest;
    /**
     * Gibt den Zinssatz an, der bei einer Auszahlung anfällt
     * zwischen 0 und 1, positiv
     */
    private double OutgoingInterest;

    /**
     * Name des Verzeichnisses, in dem serialisiert wird
     */
    private String directoryName;

    /**
     * Verknüpft konten mit Transaktionen um sie richtig zuzuordnen
     * String = name des Kontos
     */
    Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    /**
     * Liest vorhandene Konten vom Dateisystem ein und stellt sie im Klassenattribut accountsToTransactions dar
     * @throws IOException
     */
    public void readAccounts() throws IOException {
        //Alle Namen der Dateien im Ordner auslesen
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directoryName))) {
            for (Path path : stream) {
                String fileName = path.getFileName().toString();

                //Jeweilige Datei lesen
                InputStream inputStream = new FileInputStream(directoryName + "\\" + fileName);
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Transaction.class, new write_read()).create();
                JsonStreamParser p = new JsonStreamParser(reader);
                //Neuen Account anlegen
                String account_json = fileName.substring(6);
                String[] parts = account_json.split("\\."); // String array, each element is text between dots
                String account = parts[0];    // Text before the first dot
                //Liste für Accounts erstellen
                List<Transaction> transactionList = new ArrayList<>();
                //Json String durchlaufen
                while (p.hasNext()) {
                    JsonElement element = p.next();
                    //In Array umwandeln
                    JsonArray jsonElements = element.getAsJsonArray();
                    //Einzelne JsonElemente weiter verarbeiten zu Transaktionen
                    for (JsonElement jsonElement : jsonElements) {
                        Transaction transaction = gson.fromJson(jsonElement, Transaction.class);
                        //Transaktionen in accountsToTransactions speichern
                        transactionList.add(transaction);
                    }
                }
                reader.close();
                inputStream.close();
                //Neuen Account mit Transaktionen anlegen
                try {
                    if (!transactionList.isEmpty()) {
                        createAccount(account, transactionList);
                    }
                    else {
                        createAccount(account);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (DirectoryIteratorException e) {
            System.err.println(e);
        }
    }

    /**
     * Persistiert das angegebene Konto im Dateisystem
     * @param account
     * @throws IOException
     */
    public void writeAccount(String account) throws IOException, AccountDoesNotExistException {
        FileWriter fileWriter = new FileWriter(directoryName + "\\Konto " + account + ".json");
        //Transactionen zu gegebenen Konto auslesen und schreiben
        List<Transaction> transactions = new ArrayList<>(getTransactions(account));
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Transaction.class,new write_read())
                .setPrettyPrinting()
                .create();
        fileWriter.write('[');
        for (Transaction transaction : transactions) {
            String s = gson.toJson(transaction);
            if(accountsToTransactions.get(account).indexOf(transaction) != 0) {
                fileWriter.write(',');
            }
            fileWriter.write(s);
        }
        fileWriter.write(']');
        fileWriter.close();
    }

    /**
     * Konstruktor
     * @param name Name der Bank
     * @param IncomingInterest Einzahlungszinsen
     * @param OutgoingInterest Auszahlungszinsen
     */
    public PrivateBank(String name, double IncomingInterest, double OutgoingInterest, String directoryName) throws IOException {
        this.name = name;
        this.IncomingInterest = IncomingInterest;
        this.OutgoingInterest = OutgoingInterest;
        this.directoryName = directoryName;
        readAccounts();
    }

    /**
     * Copy Konstruktor
     * @param bank bestehende Bank
     */
    public PrivateBank(PrivateBank bank) throws IOException {
        this(bank.name, bank.IncomingInterest, bank.OutgoingInterest, bank.directoryName);
    }

    //Getter und Setter
    /**
     * Name Setter
     *
     * @param name Name der Bank
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Getter Name
     *
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Setter IncomingInterest
     *
     * @param incomingInterest Einzahlungszinsen
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionInvalidAttributException {
        IncomingInterest = incomingInterest;
        //TODO: Zinsen der Payments ändern
        //Jeden Account durchgehen
        for (int i = 0; i < accountsToTransactions.size(); i++) {
            //Jede Liste durchgehen
            List transactionList = accountsToTransactions.get(i);
            for (int j = 0; j < transactionList.size(); j++) {
                Object transaction = transactionList.get(j);
                if (transaction instanceof Payment payment) {
                    payment.setIncomingInterest(incomingInterest);
                }
            }
        }
    }
    /**
     * Getter IncomingInterest
     *
     * @return IncomingInterest
     */
    public double getIncomingInterest() {
        return IncomingInterest;
    }
    /**
     * Setter OutgoingInterest
     *
     * @param outgoingInterest Auszahlungszinsen
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionInvalidAttributException {
        OutgoingInterest = outgoingInterest;
        for (int i = 0; i < accountsToTransactions.size(); i++) {
            //Jede Liste durchgehen
            List transactionList = accountsToTransactions.get(i);
            for (int j = 0; j < transactionList.size(); j++) {
                Object transaction = transactionList.get(j);
                if (transaction instanceof Payment payment) {
                    payment.setOutgoingInterest(outgoingInterest);
                }
            }
        }
    }
    /**
     * Getter OutgoingInterest
     *
     * @return OutgoingInterest
     */
    public double getOutgoingInterest() {
        return OutgoingInterest;
    }
    /**
     * Setter directoryName
     * @param directoryName Name des Verzeichnisses, in dem serialisiert wird
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Getter directoryName
     * @return directoryName
     */
    public String getDirectoryName() { return directoryName; }
    /**
     * Überschreiben der toString Methode
     * @return
     */
    @Override
    public String toString() {
        return "PrivateBank{Name='" + name + '\'' +
                ", IncomingInterest='" + IncomingInterest + '\'' +
                ", OutgoingInterest='" + OutgoingInterest + '\'' +
                ", DirectoryName='" + directoryName + '\'' +
                ", AccountsToTransactions='" + accountsToTransactions + '\'' +
                "}";
    }

    /**
     * Überschreiben der equals Methode
     * @param o Objekt
     * @return
     */
    @Override
    public boolean equals(Object o) {
        // Wenn Objekte gleich sind
        if (this == o) return true;
        // Wenn Objekt null ist oder nicht vom Typ PrivateBank
        if (o == null || getClass() != o.getClass()) return false;
        // Objekt in PrivateBank umwandeln
        PrivateBank that = (PrivateBank) o;

        // Vergleichen der Attribute
        if (Double.compare(that.IncomingInterest, IncomingInterest) != 0) return false;
        if (Double.compare(that.OutgoingInterest, OutgoingInterest) != 0) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(accountsToTransactions, that.accountsToTransactions);
    }

    //Implementierung der Methoden des Interfaces Bank

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException, AccountDoesNotExistException {
        List<Transaction> transactionList = new ArrayList<>();
        //Prüfe, ob Account schon vorhanden
        if (accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists!");
        else
            accountsToTransactions.put(account, transactionList);
        //Accounts schreiben
        writeAccount(account);
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        //create Account without transactions
        createAccount(account);
        System.out.println(COLORS.ANSI_GREEN + "Account angelegt." + COLORS.ANSI_RESET);
        //add transactions to the account
        //Auf Duplikate überprüfen -> TransactionAlreadyExistException
        if (transactions.size() > 1) {
            for (int i = 0; i < transactions.size(); i++) {
                if (transactions.get(1).equals(transactions.get(0))) {
                    throw new TransactionAlreadyExistException("Duplikate in Transactions!");
                }
            }
        }


        for (Transaction transaction : transactions) {
            //Transfer auf richtigen Amount kontrollieren
            if (transaction instanceof Transfer) {
                if (transaction.amount < 0) {
                    throw new TransactionAttributeException("Transfer " + transaction.description + " vom "
                            + transaction.date + " hat negativen Betrag");
                }
                else {
                    accountsToTransactions.get(account).add(transaction);
                    System.out.println(COLORS.ANSI_GREEN + "Transaktion hinzugefügt" + COLORS.ANSI_RESET);
                }
            }
            //Payment auf richtige Zinsen kontrollieren
            else {
                //Zinsen anpassen und Payment hinzufügen
                Payment payment = (Payment) transaction;
                try {
                    payment.setIncomingInterest(this.getIncomingInterest());
                    payment.setOutgoingInterest(this.getOutgoingInterest());
                    accountsToTransactions.get(account).add(payment);
                    System.out.println(COLORS.ANSI_GREEN + "Transaktion hinzugefügt." + COLORS.ANSI_RESET);
                }
                catch (TransactionInvalidAttributException e) {
                    throw new TransactionAttributeException("Zinsen der Bank Fehlerhaft!");
                }
            }
            //Account persistieren
            writeAccount(account);
        }
    }

    /**
     * Adds a Transaction to an account
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException
     * @throws AccountDoesNotExistException
     * @throws TransactionAttributeException
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        //Lesen der Accounts
        //readAccounts();
        //Validate if Account already exists
        if (!(accountsToTransactions.containsKey(account)))
            throw new AccountDoesNotExistException("Account does not exist!");
        //Validate if transaction already exists
        List<Transaction> transactionList = accountsToTransactions.get(account);
        for (Transaction single_transaction : transactionList) {
            if (single_transaction.equals(transaction)) {
                throw new TransactionAlreadyExistException("Transaction already exists!");
            }
        }

        //Validate if Transaction Attributes are correct
        //Validate InterestRates if transaction is payment
        if (transaction instanceof Payment payment) {
            if (payment.getIncomingInterest() < 0 || payment.getIncomingInterest() > 1 ||
                    payment.getOutgoingInterest() < 0 || payment.getOutgoingInterest() > 1) {
                throw new TransactionAttributeException("Check Interest Rates!");
            }
            try {
                //Zinsen ändern auf Bankzinsen
                payment.setIncomingInterest(getIncomingInterest());
                payment.setOutgoingInterest(getOutgoingInterest());
            }
            catch (Exception e) {
                throw new TransactionAttributeException("BankZinsen sind ungültig");
            }
        }
        //Validate amount if transaction is transfer
        else if (transaction instanceof Transfer transfer) {
            if (transfer.getAmount() < 0) {
                throw new TransactionAttributeException("No negative amounts aloud!");
            }
            //Validate if sender or recipient equals account
            if (!(transfer.getSender().equals(account)) && !(transfer.getRecipient().equals(account))) {
                throw new TransactionAttributeException("Sender oder Empfänger muss gleich Account sein!");
            }
            else if (transfer.getSender().equals(account) && transfer.getRecipient().equals(account)) {
                throw new TransactionAttributeException("Sender und Empfänger dürfen nicht beide dem Accountinhaber entsprechen");
            }

        }

        //Add Transaction
        accountsToTransactions.get(account).add(transaction);
        System.out.println(COLORS.ANSI_GREEN + "Transaction erfolgreich hinzugefügt." + COLORS.ANSI_RESET);
        //Account persistieren
        writeAccount(account);
    }

    /**
     * Deletes a Transaction from an account
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException
     * @throws TransactionDoesNotExistException
     */
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if (!(accountsToTransactions.containsKey(account))) {
            throw new AccountDoesNotExistException("Account does not exist!");
        }
        else if (!(containsTransaction(account,transaction))) {
            throw new TransactionDoesNotExistException("Transaction does not exist!");
        }
        else {
            accountsToTransactions.get(account).remove(transaction);
            System.out.println(COLORS.ANSI_GREEN + "Transaction wurde erfolgreich entfernt." + COLORS.ANSI_RESET);
        }
        //Update der persistierten Accounts
        writeAccount(account);
    }

    /**
     * Checks if an account contains a transaction
     * @param account     the account which is checked
     * @param transaction the transaction which is checked
     * @return true if the account contains the transaction, false otherwise
     */
    public boolean containsTransaction(String account, Transaction transaction) {
        return accountsToTransactions.get(account).contains(transaction);
    }

    /**
     *
     * @param account the selected account
     * @return the balance of the account
     */
    public double getAccountBalance(String account) throws AccountDoesNotExistException{
        //Transaktionen durchlaufen und calculate aufsummieren
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("getAccountBalance: Account existiert nicht!");
        }
        double balance = 0;
        for (Transaction transaction : accountsToTransactions.get(account)) {
            balance += transaction.calculate();
        }
        return balance;
    }

    /**
     *
     * @param account the selected account
     * @return List of Transactions of the account
     */
    public List<Transaction> getTransactions(String account) throws IOException, AccountDoesNotExistException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("getTransactions: Account does not exist!");
        }
        return accountsToTransactions.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return List of Transactions of the account sorted by calculated amount
     */
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws IOException, AccountDoesNotExistException {
        List<Transaction> Transactions = new ArrayList<>(getTransactions(account));
        Transactions.sort(Comparator.comparing(Transaction::calculate));
        if (!asc) {
            Collections.reverse(Transactions);
        }
        return Transactions;

    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    public List<Transaction> getTransactionsByType(String account, boolean positive) throws  IOException, AccountDoesNotExistException {
        List<Transaction> Transactions = new ArrayList<>();
        if (positive) {
            for (Transaction transaction : getTransactions(account)) {
                if (transaction.calculate() > 0) {
                    Transactions.add(transaction);
                }
            }
        }
        else {
            for (Transaction transaction : getTransactions(account)) {
                if (transaction.calculate() < 0) {
                    Transactions.add(transaction);
                }
            }
        }
        return Transactions;
    }

    /**
     * Deletes the chosen Account from the Bank
     * @param account
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        //Entferne Einträge aus accountsToTransactions
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Fehler bei deleteAccount: Account existiert nicht.");
        }
        accountsToTransactions.remove(account);
        //Update persistieren, alle Accounts schreiben
        //TODO: persistieren
    }

    /**
     * Returns a List of all the Accounts from the Bank
     * @return
     */
    public List<String> getAllAccounts() {
         List accountList = new ArrayList(accountsToTransactions.keySet());
         return accountList;
    }
}



