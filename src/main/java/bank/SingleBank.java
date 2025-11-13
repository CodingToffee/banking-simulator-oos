package bank;

import java.io.IOException;

public class SingleBank {
    private static final PrivateBank  bank;

    static {
        try {
            bank = new PrivateBank("myBank",0.1,0.2,"D:\\Eigene Dateien\\Dokumente\\UNI\\OOS\\Accounts2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String accountSelected;

    private SingleBank() {
        System.out.println("singleBank erstellt.");
    }

    public static PrivateBank getInstance() {
        return bank;
    }

    public static void setAccountSelected(String account) {
        accountSelected = account;
    }

    public static String getAccountSelected() {
        return accountSelected;
    }
}
