package bank;

import bank.exceptions.TransactionInvalidAttributException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    int amount = 400;
    String date = "09.12.2022";
    String description = "Test";
    double incomingInterest = 0.1;
    double outgoingInterest = 0.15;
    Payment p0 = null;
    Payment p1 = null;

    @BeforeEach
    void init() {
        try {
            p0 = new Payment(date, amount, description);
            p1 = new Payment(date, amount, description, incomingInterest, outgoingInterest);
        }
        catch (TransactionInvalidAttributException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Konstruktor mit 3 Argumenten")
    void konstruktor_three() {
        assertEquals(Payment.class,p0.getClass());
        assertEquals(date,p0.getDate());
        assertEquals(amount,p0.getAmount());
        assertEquals(description,p0.getDescription());
    }

    @Test
    @DisplayName("Konstruktor mit 5 Argumenten")
    void konstruktor_five() {
        assertEquals(Payment.class,p1.getClass());
        assertEquals(date,p1.getDate());
        assertEquals(amount,p1.getAmount());
        assertEquals(description,p1.getDescription());
        assertEquals(incomingInterest,p1.getIncomingInterest());
        assertEquals(outgoingInterest,p1.getOutgoingInterest());
    }

    @Test
    @DisplayName("Copyconstructor")
    void konstruktor_copy() throws TransactionInvalidAttributException {
        Payment p2 = new Payment(p0);
        assertEquals(p0,p2);
    }

    @Test
    @DisplayName("toString Methode")
    void testToString() {
        assertEquals("Payment{date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", amount='" + p1.calculate() + '\'' +
                ", incomingInterest='" + incomingInterest + '\'' +
                ", outgoingInterest='" + outgoingInterest + "\'}",p1.toString());
    }

    @Test
    @DisplayName("Calculate Methode")
    void calculate() {
        assertEquals(amount-(amount*incomingInterest),p1.calculate());
        double amount_neg = -400;
        p1.setAmount(amount_neg);
        assertEquals(amount_neg+(amount_neg*outgoingInterest),p1.calculate());
    }

    @Test
    @DisplayName("Equals Methode")
    void testEquals() {
        assertFalse(p0.equals(p1));
        assertFalse(p0.equals(null));
        assertTrue(p0.equals(p0));
        Payment p0_twin = new Payment(date,amount,description);
        assertTrue(p0.equals(p0_twin));
    }
}