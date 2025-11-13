package bank;

import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionInvalidAttributException;

public class Payment extends Transaction implements CalculateBill {
    /**
     * Gibt Zinsen an, die bei Einzahlung anfallen
     */
    private double incomingInterest;
    /**
     * gibt Zinsen an, die bei Auszahlung anfallen
     */
    private double outgoingInterest;

    /**
     * Setter für Geldmenge
     * @param amt: Geldmenge
     */
    public void setAmount(double amt) {
        this.amount = amt;
    }
    /**
     * Getter für Einzahlungszinsen
     * @return Einzahlungszinsen
     */
    public double getIncomingInterest(){
        return incomingInterest;
    }

    /**
     *Setter für Einzahlungszinsen überprüft korrektheit der Eingabe
     * @param I IncomingInterest
     */
    public void setIncomingInterest(double I) throws TransactionInvalidAttributException {
        if (I <= 1 && I >= 0)
            this.incomingInterest = I;
        else
            throw new TransactionInvalidAttributException("Fehlerhafte Eingabe. Zinsen müssen zwischen 0 und 1 sein.");
    }

    /**
     * Getter für Auszahlungen
     * @return Auszahlungszinsen
     */
    public double getOutgoingInterest(){
        return outgoingInterest;
    }

    /**
     * Setter für Auszahlungszinsen, überprüft Korrektheit der Eingabe
     * @param I Auszahlungszinsen (Interest)
     */
    public void setOutgoingInterest(double I) throws TransactionInvalidAttributException {
        if (I <= 1 && I >= 0)
            this.outgoingInterest = I;
        else
            throw new TransactionInvalidAttributException("Fehlerhafte Eingabe. Zinsen müssen zwischen 0 und 1 sein.");
    }
    /**
     * Konstruktor
     * @param dte: Datum
     * @param amt: Geldmenge
     * @param desc: Beschreibung
     */
    public Payment(String dte, double amt, String desc) {
        super(dte, amt, desc);
    }

    /**
     * //Konstruktor mit allen Argumenten in obiger Reihenfolge
     * @param dte: Datum
     * @param amt: Geldmenge
     * @param desc: Beschreibung
     * @param incInterest: Einzahlungszinsen
     * @param outInterest: Auszahlungszinsen
     */
    public Payment(String dte, double amt, String desc, double incInterest, double outInterest) throws TransactionInvalidAttributException {
        this(dte, amt, desc);
        setIncomingInterest(incInterest);
        setOutgoingInterest(outInterest);
    }

    /**
     * Copy Konstruktor
     * @param pmt Objekt Payment
     */
    public Payment(Payment pmt) throws TransactionInvalidAttributException{
        this(pmt.date, pmt.amount, pmt.description, pmt.incomingInterest, pmt.outgoingInterest);
    }

    /**
     * Überschreibt toString() Methode
     * @return Geldmenge, Einzahlungszinsen und Auszahlungszinsen als String
     */
    @Override
    public String toString() {
        return "Payment{" + super.toString() +
                ", incomingInterest='" + incomingInterest + '\'' +
                ", outgoingInterest='" + outgoingInterest + '\'' +
                '}';
    }

    /**
     * Berechnet tatsächlichen Betrag auf Konto
     * zu-/abzüglich Zinsen
     * @return tatsächlicher Betrag
     */
    @Override
    public double calculate() {
        if (amount > 0)
            return (double)(amount - (amount * incomingInterest));
        else
            return (double)(amount + (amount * outgoingInterest));
    }

    /**
     * Vergleicht, ob zwei Objekte identisch sind
     * @param o Objekt, dass verglichen werden soll
     * @return Gibt wahr oder falsch zurück
     */
    @Override
    public boolean equals(Object o) {
        if (!(super.equals(o))) {
            return false;
        }
        else {
            //Vergleiche, ob gleiche Klasse
            if (!(o instanceof Payment))
                return false;
            else {
                //Typecast um Attribute vergleichen zu können
                Payment p = (Payment) o;

                //Attribute vergleichen
                return (incomingInterest == p.incomingInterest) &&
                        (outgoingInterest == p.outgoingInterest);
            }
        }
    }
}
