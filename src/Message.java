package simpleloginsystem;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Message {
    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private boolean isSent;
    private static final String JSON_FILE = "messages.json";

    // Constructor with message number parameter
    public Message(String recipient, String messageText, int messageNumber) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash(messageNumber);
        this.isSent = false;
    }

    // Generate 10-digit random message ID
    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

    // Create message hash in format twoDigits:messageNumber:FIRSTLAST
    public String createMessageHash(int messageNumber) {
        String firstTwoID = messageID.substring(0, 2);

        // Extract first and last word, remove non-alphanumeric characters, convert to uppercase
        String[] words = messageText.split("\\s+");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;

        // Remove non-alphanumeric characters and convert to uppercase
        firstWord = firstWord.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        lastWord = lastWord.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        return firstTwoID + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    // Store message in JSON file
    public void storeMessage() {
        try {
            JSONObject messageJson = new JSONObject();
            messageJson.put("messageID", this.messageID);
            messageJson.put("recipient", this.recipient);
            messageJson.put("messageText", this.messageText);
            messageJson.put("messageHash", this.messageHash);
            messageJson.put("isSent", this.isSent);
            messageJson.put("timestamp", java.time.LocalDateTime.now().toString());

            JSONArray messagesArray;
            File file = new File(JSON_FILE);
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
                messagesArray = new JSONArray(content);
            } else {
                messagesArray = new JSONArray();
            }

            messagesArray.put(messageJson);

            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(messagesArray.toString(4));
            }
        } catch (IOException e) {
            System.err.println("Error storing message: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public boolean isSent() { return isSent; }

    @Override
    public String toString() {
        return "Message ID: " + messageID + "\n" +
                "Message Hash: " + messageHash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + messageText;
    }
}