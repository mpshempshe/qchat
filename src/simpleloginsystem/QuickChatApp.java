package simpleloginsystem;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class QuickChatApp {
    private static Login authSystem;
    private static MessageStore messageStore;
    private static Scanner scanner;
    private static int messageLimit;
    private static int messagesProcessed = 0;
    private static int messageCounter = 0;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        authSystem = new Login();
        messageStore = new MessageStore();

        // Show welcome message using JOptionPane
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat");

        // User Registration
        System.out.println("=== User Registration ===");
        String registrationResult = authSystem.registerNewUser(scanner);
        System.out.println(registrationResult);

        if (!registrationResult.toLowerCase().contains("successful")) {
            System.out.println("Registration failed. Exiting application.");
            scanner.close();
            return;
        }

        // User Login
        System.out.println("\n=== User Login ===");
        System.out.print("Enter username: ");
        String enteredUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String enteredPassword = scanner.nextLine();

        boolean loginSuccess = authSystem.authenticateUser(enteredUsername, enteredPassword);
        System.out.println(authSystem.getLoginStatusMessage(loginSuccess));

        if (!loginSuccess) {
            System.out.println("Login failed. Exiting application.");
            scanner.close();
            return;
        }

        // Get message limit
        System.out.print("\nHow many messages do you want to enter? ");
        messageLimit = Integer.parseInt(scanner.nextLine());

        // Main messaging loop
        while (messagesProcessed < messageLimit) {
            processMessage();
        }

        // Show total messages sent
        System.out.println("\nTotal messages sent: " + messageStore.getSentMessages().size());

        // Part 3 Menu
        showPart3Menu();

        scanner.close();
    }

    private static void processMessage() {
        // Get recipient
        System.out.print("\nEnter recipient (+<code><number>, <=10 digits total): ");
        String recipient = scanner.nextLine().trim();

        // Validate recipient format - must start with + and have exactly 10 digits after
        if (!recipient.matches("^\\+\\d{10}$")) {
            System.out.println("Cell phone number is incorrectly formatted or does not contain an international code...");
            return;
        }

        // Get message text
        System.out.print("Enter message (<= 250 characters): ");
        String messageText = scanner.nextLine().trim();

        // Validate message length
        if (messageText.length() > 250) {
            int excess = messageText.length() - 250;
            System.out.println("Message exceeds 250 characters by " + excess + ", please reduce size.");
            return;
        }

        // Create message
        Message message = new Message(recipient, messageText, messageCounter);
        messageCounter++;

        // Display message details in exact order (as per PDF)
        System.out.println("MessageID: " + message.getMessageID());
        System.out.println("Message Hash: " + message.getMessageHash());
        System.out.println("Recipient: " + message.getRecipient());
        System.out.println("Message: " + message.getMessageText());

        // Choose action
        System.out.println("\nChoose what to do with this message:");
        System.out.println("1. Send Message");
        System.out.println("2. Disregard Message");
        System.out.println("3. Store Message");
        System.out.print("Enter your choice (1-3): ");

        String choiceInput = scanner.nextLine();
        int choice;
        try {
            choice = Integer.parseInt(choiceInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice. Message disregarded.");
            return;
        }

        switch (choice) {
            case 1: // Send
                messageStore.addSentMessage(message, authSystem.getFirstName() + " " + authSystem.getLastName());
                System.out.println("Message successfully sent.");
                messagesProcessed++;
                break;

            case 2: // Disregard
                System.out.print("Press 0 to delete message: ");
                String input = scanner.nextLine();
                if ("0".equals(input)) {
                    messageStore.addDisregardedMessage(message);
                    System.out.println("Message disregarded.");
                } else {
                    System.out.println("Message kept.");
                }
                // Don't increment messagesProcessed for disregarded messages
                break;

            case 3: // Store
                message.storeMessage();
                messageStore.addStoredMessage(message);
                System.out.println("Message successfully stored.");
                messagesProcessed++;
                break;

            default:
                System.out.println("Invalid choice. Message disregarded.");
        }
    }

    private static void showPart3Menu() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Message Store Operations ===");
            System.out.println("a) List sender & recipient of all Sent messages");
            System.out.println("b) Show longest Sent message");
            System.out.println("c) Search by Message ID");
            System.out.println("d) Find all messages for a Recipient");
            System.out.println("e) Delete by Message Hash");
            System.out.println("f) Print a Sent Report");
            System.out.println("x) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a":
                    listSendersAndRecipients();
                    break;
                case "b":
                    showLongestMessage();
                    break;
                case "c":
                    searchByMessageId();
                    break;
                case "d":
                    findByRecipient();
                    break;
                case "e":
                    deleteByMessageHash();
                    break;
                case "f":
                    printSentReport();
                    break;
                case "x":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void listSendersAndRecipients() {
        String result = messageStore.listSendersAndRecipients();
        System.out.println("\n" + result);
    }

    private static void showLongestMessage() {
        String result = messageStore.showLongestMessage();
        System.out.println("\n" + result);
    }

    private static void searchByMessageId() {
        System.out.print("Enter 10-digit Message ID: ");
        String messageId = scanner.nextLine().trim();
        String result = messageStore.searchByMessageId(messageId);
        System.out.println("\n" + result);
    }

    private static void findByRecipient() {
        System.out.print("Enter recipient number (e.g., +27838884567): ");
        String recipient = scanner.nextLine().trim();
        String result = messageStore.findByRecipient(recipient);
        System.out.println("\n" + result);
    }

    private static void deleteByMessageHash() {
        System.out.print("Enter Message Hash (e.g., 00:3:HITONIGHT): ");
        String hash = scanner.nextLine().trim();
        String result = messageStore.deleteByMessageHash(hash);
        System.out.println("\n" + result);
    }

    private static void printSentReport() {
        String result = messageStore.printSentReport();
        System.out.println("\n" + result);
    }
}