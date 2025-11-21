package simpleloginsystem;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageStore {
    private List<Message> sentMessages;
    private List<Message> storedMessages;
    private List<Message> disregardedMessages;
    private Map<Message, String> messageSenders;

    public MessageStore() {
        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();
        messageSenders = new HashMap<>();
        loadStoredMessages();
    }

    // Load stored messages from JSON file on startup
    private void loadStoredMessages() {
        try {
            File file = new File("messages.json");
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get("messages.json")));
                JSONArray messagesArray = new JSONArray(content);

                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject msgJson = messagesArray.getJSONObject(i);
                    // Create message with placeholder message number (0)
                    Message message = new Message(
                            msgJson.getString("recipient"),
                            msgJson.getString("messageText"),
                            0
                    );
                    storedMessages.add(message);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading stored messages: " + e.getMessage());
        }
    }

    // Add methods for different message types
    public void addSentMessage(Message message, String sender) {
        sentMessages.add(message);
        messageSenders.put(message, sender);
    }

    public void addStoredMessage(Message message) {
        storedMessages.add(message);
    }

    public void addDisregardedMessage(Message message) {
        disregardedMessages.add(message);
    }

    // Getters
    public List<Message> getSentMessages() { return sentMessages; }
    public List<Message> getStoredMessages() { return storedMessages; }
    public List<Message> getDisregardedMessages() { return disregardedMessages; }

    // Part 3 Operations

    // a) List sender & recipient of all Sent messages
    public String listSendersAndRecipients() {
        if (sentMessages.isEmpty()) {
            return "No sent messages yet.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            String sender = messageSenders.get(msg);
            sb.append("Sender: ").append(sender).append("\n")
                    .append("Recipient: ").append(msg.getRecipient()).append("\n")
                    .append("---\n");
        }
        return sb.toString();
    }

    // b) Show longest Sent message
    public String showLongestMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages yet.";
        }

        Message longest = sentMessages.get(0);
        for (Message msg : sentMessages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }

        return "Longest Message: " + longest.getMessageText();
    }

    // c) Search by Message ID
    public String searchByMessageId(String messageId) {
        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getMessageID().equals(messageId)) {
                return "Recipient: " + msg.getRecipient() + "\nMessage: " + msg.getMessageText();
            }
        }

        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getMessageID().equals(messageId)) {
                return "Recipient: " + msg.getRecipient() + "\nMessage: " + msg.getMessageText();
            }
        }

        return "Message ID not found.";
    }

    // d) Find all messages for a Recipient
    public String findByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        // Search in sent messages
        for (Message msg : sentMessages) {
            if (msg.getRecipient().equals(recipient)) {
                sb.append("Message: ").append(msg.getMessageText()).append("\n")
                        .append("Message ID: ").append(msg.getMessageID()).append("\n")
                        .append("---\n");
                found = true;
            }
        }

        // Search in stored messages
        for (Message msg : storedMessages) {
            if (msg.getRecipient().equals(recipient)) {
                sb.append("Message: ").append(msg.getMessageText()).append("\n")
                        .append("Message ID: ").append(msg.getMessageID()).append("\n")
                        .append("---\n");
                found = true;
            }
        }

        if (!found) {
            return "No messages found for recipient: " + recipient;
        }

        return sb.toString();
    }

    // e) Delete by Message Hash
    public String deleteByMessageHash(String hash) {
        // Search in sent messages
        Iterator<Message> sentIterator = sentMessages.iterator();
        while (sentIterator.hasNext()) {
            Message msg = sentIterator.next();
            if (msg.getMessageHash().equals(hash)) {
                String messageText = msg.getMessageText();
                sentIterator.remove();
                messageSenders.remove(msg);
                return "Message \"" + messageText + "\" successfully deleted.";
            }
        }

        // Search in stored messages
        Iterator<Message> storedIterator = storedMessages.iterator();
        while (storedIterator.hasNext()) {
            Message msg = storedIterator.next();
            if (msg.getMessageHash().equals(hash)) {
                String messageText = msg.getMessageText();
                storedIterator.remove();
                removeFromJSON(msg);
                return "Message \"" + messageText + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    // Remove message from JSON file
    private void removeFromJSON(Message messageToRemove) {
        try {
            File file = new File("messages.json");
            if (file.exists()) {
                String content = new String(Files.readAllBytes(Paths.get("messages.json")));
                JSONArray messagesArray = new JSONArray(content);
                JSONArray newArray = new JSONArray();

                for (int i = 0; i < messagesArray.length(); i++) {
                    JSONObject msgJson = messagesArray.getJSONObject(i);
                    if (!msgJson.getString("messageID").equals(messageToRemove.getMessageID())) {
                        newArray.put(msgJson);
                    }
                }

                try (FileWriter writer = new FileWriter("messages.json")) {
                    writer.write(newArray.toString(4));
                }
            }
        } catch (IOException e) {
            System.err.println("Error removing message from JSON: " + e.getMessage());
        }
    }

    // f) Print a Sent Report
    public String printSentReport() {
        if (sentMessages.isEmpty()) {
            return "No sent messages yet.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            sb.append("Message Hash: ").append(msg.getMessageHash()).append("\n")
                    .append("Recipient: ").append(msg.getRecipient()).append("\n")
                    .append("Message: ").append(msg.getMessageText()).append("\n")
                    .append("---\n");
        }
        return sb.toString();
    }
}