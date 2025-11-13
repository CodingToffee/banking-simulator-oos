package bank;

import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionInvalidAttributException;

public abstract class Transaction implements CalculateBill{
    /**
     * Datums-Attribut DD.MM.JJJJ
     */
    String date;
    /**
     * Gibt Geldmenge der Ein- oder Auszahlungen an
     */
    double amount;
    /**
     * erlaubt zusätzliche Beschreibung des Vorgangs
     */
    String description;

    /**
     * Konstruktor
     * @param date Datum
     * @param amount Geldmenge
     * @param description Beschreibung
     */
    Transaction(String date, double amount, String description) {
        try {
            this.setDate(date); //setter verwenden
            this.setAmount(amount);
            this.setDescription(description);
        }
        catch (TransactionInvalidAttributException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Getter für Datum
     * @return Datum
     */
    public String getDate(){
        return date;
    }

    /**
     * Setter für Datum
     * @param d Datum
     */
    private void setDate(String d){
        this.date = d;
    }

    /**
     * Getter für Geldmenge
     * @return Geldmenge
     */
    public double getAmount(){
        return amount;
    }

    /**
     * Setter für Geldmenge
     * @param a Geldmenge
     */
    abstract void setAmount(double a) throws TransactionInvalidAttributException;

    /**
     * Getter für zusätzliche Beschreibung
     * @return zusätzliche Beschreibung
     */
    public String getDescription(){
        return description;
    }

    /**
     * Setter für zusätzliche Beschreibung
     * @param d Beschreibung
     */
    private void setDescription(String d){
        this.description = d;
    }

    /**
     * Überschreibt toString Methode
     * @return Datum u. Beschreibung als String
     */
    @Override
    public String toString() {
        return "date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + this.calculate() + '\'';
    }

    /**
     * Vergleicht, ob zwei Objekte identisch sind
     * @param o Objekt, dass verglichen werden soll
     * @return Gibt wahr oder falsch zurück
     */
    @Override
    public boolean equals(Object o) {
            //Prüfe, ob t selbes Objekt ist
            if (o == this) {
                return true;
            }

            //Prüfe, ob gleiche Klasse
            if (!(o instanceof Transaction)) {
                return false;
            }

            //Typecast zu Transaction um Attribute vergleichen zu können
            Transaction t = (Transaction) o;

            //Vergleiche Attribute //mit equals verlgeichen
            return date.equals(t.date)
                    && amount == t.amount
                    && description.equals(t.description);
    }
}
