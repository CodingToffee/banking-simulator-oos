package bank;

import bank.exceptions.TransactionAttributeException;
import bank.exceptions.TransactionInvalidAttributException;

import javax.xml.transform.TransformerFactory;
import java.util.Objects;

//Überweisungsklasse
public class Transfer extends Transaction implements CalculateBill{
    /**
     * Gibt an, wer das Geld gesendet hat
     */
    private String sender;
    /**
     * Gibt an, wer das Geld erhalten hat
     */
    private String recipient;

    //Getter and Setter
    /**
     * Setter für Geldmenge, prüft auf negative Beträge
     * @param a Geldmenge
     */
    public void setAmount(double a) throws TransactionInvalidAttributException {
        if (a < 0) {
            throw new TransactionInvalidAttributException("Keine negativen Beträge erlaubt.");
        }
        else
            this.amount = a;
    }

    /**
     * Getter für Sender
     * @return Sender
     */
    public String getSender(){
        return sender;
    }

    /**
     * Setter für Sender
     * @param s Sender
     */
    public void setSender(String s){
        this.sender = s;
    }

    /**
     * Getter für Empfänger
     * @return Empfänger
     */
    public String getRecipient(){
        return recipient;
    }

    /**
     * Setter für Empfänger
     * @param r Empfänger
     */
    public void setRecipient(String r){
        this.recipient = r;
    }

    //Konstruktoren
    /**
     * Konstruktor
     * @param dte: Datum
     * @param amt: Geldmenge
     * @param desc: Beschreibung
     */
    public Transfer(String dte, double amt, String desc) throws TransactionInvalidAttributException{
        super(dte, amt, desc);
        if (amt < 0) {
            throw new TransactionInvalidAttributException("Der Betrag eines Transfers darf nicht negativ sein!");
        }

    }

    /**
     * Konstruktor mit allen Attributen
     * @param dte: Datum
     * @param amt: Geldmenge
     * @param desc: Beschreibung
     * @param snd: Sender
     * @param rcp: Empfänger
     */
    public Transfer(String dte, double amt, String desc, String snd, String rcp) throws TransactionInvalidAttributException{ //Konstruktor mit allen Argumenten in obiger Reihenfolge
        this(dte, amt, desc);
        if (amt < 0) {
            throw new TransactionInvalidAttributException("Der Betrag eines Transfers darf nicht negativ sein!");
        }
        this.setSender(snd);
        this.setRecipient(rcp);
    }

    /**
     * Copy Konstruktor
     * @param trf : Objekt Transfer
     */
    public Transfer(Transfer trf) throws TransactionInvalidAttributException {
        this(trf.date, trf.amount, trf.description, trf.sender, trf.recipient);
    }

    /**
     * Überschreibt toString() Methode
     * @return Geldmenge, Sender u. Empfänger als String
     */
    @Override
    public String toString() {
        return "Transfer{" + super.toString() +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    /**
     * Vergleicht, ob zwei Objekte identisch sind
     * @param o Objekt, dass verglichen werden soll
     * @return Gibt wahr falsch zurück
     */
    @Override
    public boolean equals(Object o) {
        if (!(super.equals(o))) {
            return false;
        }
        else {
            //Vergleiche, ob gleiche Klasse
            if (!(o instanceof Transfer))
                return false;
            else {
                //Typecast um Attribute vergleichen zu können
                Transfer t = (Transfer) o;

                //Vergleiche Attribute
                return (Objects.equals(sender, t.sender)) &&
                        (Objects.equals(recipient, t.recipient));
            }
        }

     }

    /**
     * Implementiert calculate(); aus CalculateBill
     * @return amount ohne Veränderung
     */
    @Override
    public double calculate() {
        return amount;
    }
}
