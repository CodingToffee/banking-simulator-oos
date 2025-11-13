package bank;

import bank.*;
import bank.exceptions.TransactionInvalidAttributException;
import com.google.gson.*;

import java.lang.reflect.Type;

public class write_read implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    @Override
    public Transaction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        //JsonElement in Payment oder Transfer umwandeln
        JsonObject jsonObject = jsonElement.getAsJsonObject();


        if (jsonObject.get("CLASSNAME").getAsString().equals("Payment")) {
            JsonObject Instance = jsonObject.get("INSTANCE").getAsJsonObject();
            try {
                return new Payment(
                        Instance.get("date").getAsString(),
                        Instance.get("amount").getAsDouble(),
                        Instance.get("description").getAsString(),
                        Instance.get("incomingInterest").getAsDouble(),
                        Instance.get("outgoingInterest").getAsDouble()
                );
            }
            catch (TransactionInvalidAttributException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (jsonObject.get("CLASSNAME").getAsString().equals("IncomingTransfer")) {
            JsonObject Instance = jsonObject.get("INSTANCE").getAsJsonObject();
            try {
                return new IncomingTransfer(
                        Instance.get("date").getAsString(),
                        Instance.get("amount").getAsDouble(),
                        Instance.get("description").getAsString(),
                        Instance.get("sender").getAsString(),
                        Instance.get("recipient").getAsString()
                );
            }
            catch (TransactionInvalidAttributException e) {
                System.out.println(e.getMessage());
            }
        }
        else if (jsonObject.get("CLASSNAME").getAsString().equals("Transfer")) {
            JsonObject Instance = jsonObject.get("INSTANCE").getAsJsonObject();
            try {
                return new Transfer(
                        Instance.get("date").getAsString(),
                        Instance.get("amount").getAsDouble(),
                        Instance.get("description").getAsString(),
                        Instance.get("sender").getAsString(),
                        Instance.get("recipient").getAsString()
                );
            } catch (TransactionInvalidAttributException e) {
                System.out.println(e.getMessage());
            }
        }
        else {
            JsonObject Instance = jsonObject.get("INSTANCE").getAsJsonObject();
            try {
                return new OutgoingTransfer(
                        Instance.get("date").getAsString(),
                        Instance.get("amount").getAsDouble(),
                        Instance.get("description").getAsString(),
                        Instance.get("sender").getAsString(),
                        Instance.get("recipient").getAsString()
                );
            }
            catch (TransactionInvalidAttributException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject JsonTransaction = new JsonObject();
        JsonObject FullJ = new JsonObject();

        JsonTransaction.addProperty("date", transaction.getDate());
        JsonTransaction.addProperty("amount", transaction.getAmount());
        JsonTransaction.addProperty("description", transaction.getDescription());

        if (transaction instanceof Transfer transfer) {
            JsonTransaction.addProperty("sender", transfer.getSender());
            JsonTransaction.addProperty("recipient", transfer.getRecipient());
        }
        if (transaction instanceof Payment payment) {
            JsonTransaction.addProperty("incomingInterest", payment.getIncomingInterest());
            JsonTransaction.addProperty("outgoingInterest", payment.getOutgoingInterest());
        }

        FullJ.addProperty("CLASSNAME",transaction.getClass().getSimpleName());
        FullJ.add("INSTANCE",JsonTransaction);

        return FullJ;
    }
}
