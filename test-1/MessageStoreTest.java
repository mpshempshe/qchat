import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import simpleloginsystem.Message;
import simpleloginsystem.MessageStore;

import static org.junit.jupiter.api.Assertions.*;

public class MessageStoreTest {
    private MessageStore messageStore;
    private Message message1;
    private Message message2;
    private Message message3;

    @BeforeEach
    public void setUp() {
        messageStore = new MessageStore();
        message1 = new Message("+27831234567", "First test message", 0);
        message2 = new Message("+27831234568", "Second test message that is longer", 1);
        message3 = new Message("+27831234567", "Third message for same recipient", 2);
    }

    @Test
    public void testAddSentMessage() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addSentMessage(message2, "Jane Smith");

        assertEquals(2, messageStore.getSentMessages().size());
        assertEquals(0, messageStore.getStoredMessages().size());
        assertEquals(0, messageStore.getDisregardedMessages().size());
    }

    @Test
    public void testAddStoredMessage() {
        messageStore.addStoredMessage(message1);
        messageStore.addStoredMessage(message2);

        assertEquals(0, messageStore.getSentMessages().size());
        assertEquals(2, messageStore.getStoredMessages().size());
        assertEquals(0, messageStore.getDisregardedMessages().size());
    }

    @Test
    public void testAddDisregardedMessage() {
        messageStore.addDisregardedMessage(message1);
        messageStore.addDisregardedMessage(message2);

        assertEquals(0, messageStore.getSentMessages().size());
        assertEquals(0, messageStore.getStoredMessages().size());
        assertEquals(2, messageStore.getDisregardedMessages().size());
    }

    @Test
    public void testListSendersAndRecipients() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addSentMessage(message2, "Jane Smith");

        String result = messageStore.listSendersAndRecipients();

        assertTrue(result.contains("Sender: John Doe"));
        assertTrue(result.contains("Recipient: +27831234567"));
        assertTrue(result.contains("Sender: Jane Smith"));
        assertTrue(result.contains("Recipient: +27831234568"));
    }

    @Test
    public void testListSendersAndRecipientsEmpty() {
        String result = messageStore.listSendersAndRecipients();
        assertEquals("No sent messages yet.", result);
    }

    @Test
    public void testShowLongestMessage() {
        messageStore.addSentMessage(message1, "John Doe"); // "First test message" (18 chars)
        messageStore.addSentMessage(message2, "Jane Smith"); // "Second test message that is longer" (33 chars)

        String result = messageStore.showLongestMessage();

        assertTrue(result.contains("Longest Message: Second test message that is longer"));
    }

    @Test
    public void testShowLongestMessageEmpty() {
        String result = messageStore.showLongestMessage();
        assertEquals("No sent messages yet.", result);
    }

    @Test
    public void testSearchByMessageId() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addStoredMessage(message2);

        String messageId = message1.getMessageID();
        String result = messageStore.searchByMessageId(messageId);

        assertTrue(result.contains("Recipient: +27831234567"));
        assertTrue(result.contains("Message: First test message"));
    }

    @Test
    public void testSearchByMessageIdNotFound() {
        String result = messageStore.searchByMessageId("9999999999");
        assertEquals("Message ID not found.", result);
    }

    @Test
    public void testFindByRecipient() {
        messageStore.addSentMessage(message1, "John Doe"); // +27831234567
        messageStore.addSentMessage(message2, "Jane Smith"); // +27831234568
        messageStore.addStoredMessage(message3); // +27831234567

        String result = messageStore.findByRecipient("+27831234567");

        // Should find both sent and stored messages for this recipient
        assertTrue(result.contains("First test message"));
        assertTrue(result.contains("Third message for same recipient"));
        assertFalse(result.contains("Second test message")); // different recipient
    }

    @Test
    public void testFindByRecipientNotFound() {
        String result = messageStore.findByRecipient("+27830000000");
        assertTrue(result.contains("No messages found for recipient: +27830000000"));
    }

    @Test
    public void testDeleteByMessageHash() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addStoredMessage(message2);

        String hash = message1.getMessageHash();
        String result = messageStore.deleteByMessageHash(hash);

        assertTrue(result.contains("successfully deleted"));
        assertEquals(0, messageStore.getSentMessages().size());
        assertEquals(1, messageStore.getStoredMessages().size());
    }

    @Test
    public void testDeleteByMessageHashNotFound() {
        String result = messageStore.deleteByMessageHash("00:999:NOTFOUND");
        assertEquals("Message hash not found.", result);
    }

    @Test
    public void testPrintSentReport() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addSentMessage(message2, "Jane Smith");

        String result = messageStore.printSentReport();

        assertTrue(result.contains("Message Hash: " + message1.getMessageHash()));
        assertTrue(result.contains("Recipient: +27831234567"));
        assertTrue(result.contains("Message: First test message"));
        assertTrue(result.contains("Message Hash: " + message2.getMessageHash()));
        assertTrue(result.contains("Recipient: +27831234568"));
        assertTrue(result.contains("Message: Second test message that is longer"));
    }

    @Test
    public void testPrintSentReportEmpty() {
        String result = messageStore.printSentReport();
        assertEquals("No sent messages yet.", result);
    }

    @Test
    public void testMixedMessageTypes() {
        messageStore.addSentMessage(message1, "John Doe");
        messageStore.addStoredMessage(message2);
        messageStore.addDisregardedMessage(message3);

        assertEquals(1, messageStore.getSentMessages().size());
        assertEquals(1, messageStore.getStoredMessages().size());
        assertEquals(1, messageStore.getDisregardedMessages().size());
    }
}