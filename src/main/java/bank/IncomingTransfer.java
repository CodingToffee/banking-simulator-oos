package bank;

import bank.exceptions.TransactionInvalidAttributException;

public class IncomingTransfer extends Transfer {
    /**
     * Konstruktor
     * @param dte
     * @param amt
     * @param desc
     */
    public IncomingTransfer(String dte, double amt, String desc) throws TransactionInvalidAttributException {
        super(dte, amt, desc);
    }

    public IncomingTransfer(String dte, double amt, String desc, String snd, String rcp) throws TransactionInvalidAttributException {
        super(dte, amt, desc, snd, rcp);
    }
    /**
     * Überschreibt Calculate Methode um zwischen Eingehenden- und Ausgehenden Transfers zu unterscheiden
     * @return Kalkulierter Betrag
     */
    @Override
    public double calculate() {
        return amount;
    }
}
