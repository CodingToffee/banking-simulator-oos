package bank;

import bank.exceptions.TransactionInvalidAttributException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    String date0 = "01.01.2020";
    String date1 = "02.02.2021";
    double amount0 = 100;
    double amount1 = 200;
    String sender0 = "A";
    String sender1 = "B";
    String recipient0 = "B";
    String recipient1 = "C";
    String description0 = "Test1";
    String description1 = "Test2";

    Transfer t0 = null;
    Transfer t0_copy = null;
    Transfer t1 = null;
    Transfer t1_copy = null;
    Transfer copy = null;

    @BeforeEach
    void init() {
        try {
            t0 = new Transfer(date0, amount0, description0);
            t1 = new Transfer(date1, amount1, description1, sender1, recipient1);
            t0_copy = new Transfer(date0, amount0, description0);
            t1_copy = new Transfer(date1, amount1, description1, sender1, recipient1);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        t0.setRecipient(recipient0);
        t0.setSender(sender0);
        t0_copy.setSender(sender0);
        t0_copy.setRecipient(recipient0);
    }

    @Test
    @DisplayName("Konstruktor mit 3 Argumenten")
    void testKonstruktor_3() {
        assertThrows(TransactionInvalidAttributException.class, () -> new Transfer(date0, -200, description0));
        assertEquals(date0, t0.getDate());
        assertEquals(amount0, t0.getAmount());
        assertEquals(description0, t0.getDescription());
        assertEquals(Transfer.class,t0.getClass());
    }

    @Test
    @DisplayName("Konstruktor mit 5 Argumenten")
    void TestKonstruktor_5() {
        assertThrows(TransactionInvalidAttributException.class,() -> new Transfer(date1,-200,description1,sender1,recipient1));
        assertEquals(date0, t0.getDate());
        assertEquals(amount0, t0.getAmount());
        assertEquals(description0, t0.getDescription());
        assertEquals(sender0, t0.getSender());
        assertEquals(recipient0, t0.getRecipient());
        assertEquals(Transfer.class,t0.getClass());
    }

    @Test
    @DisplayName("Copy Konstruktor")
    void testCopyKonstruktor() {
        try {
            copy = new Transfer(t0);
        }
        catch (TransactionInvalidAttributException e) {
            System.out.println(e.getMessage());
        }
        assertEquals(date0, copy.getDate());
        assertEquals(amount0, copy.getAmount());
        assertEquals(description0, copy.getDescription());
        assertEquals(sender0, copy.getSender());
        assertEquals(recipient0, copy.getRecipient());
        assertEquals(Transfer.class,copy.getClass());
    }

    @Test
    void testToString() {
        assertEquals("Transfer{" +
                "date='" + date0 + '\'' +
                ", description='" + description0 + '\'' +
                ", amount='" + amount0 + '\'' +
                ", sender='" + sender0 + '\'' +
                ", recipient='" + recipient0 + '\'' +
                '}', t0.toString());
    }

    @Test
    void testEquals() {
        assertTrue(t0.equals(t0_copy));
        assertTrue(t1.equals(t1_copy));
        assertFalse(t0.equals(t1));
        assertFalse(t0.equals(t1_copy));
    }

    @Test
    void calculate() {
        assertEquals(t0.getAmount(),t0.calculate());
        assertEquals(t1.getAmount(),t1.calculate());
    }
}