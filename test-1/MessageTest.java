import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import simpleloginsystem.Message;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.io.File;

public class MessageTest {
    private Message message;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        message = new Message("+27831234567", "Hello World", 0);
    }

    @Test
    public void testMessageConstructor() {
        assertNotNull(message.getMessageID());
        assertEquals("+27831234567", message.getRecipient());
        assertEquals("Hello World", message.getMessageText());
        assertFalse(message.isSent());
    }

    @Test
    public void testGenerateMessageID() {
        Message msg1 = new Message("+27831234567", "Test1", 0);
        Message msg2 = new Message("+27831234568", "Test2", 1);

        assertNotNull(msg1.getMessageID());
        assertNotNull(msg2.getMessageID());
        assertEquals(10, msg1.getMessageID().length());
        assertEquals(10, msg2.getMessageID().length());
        assertTrue(msg1.getMessageID().matches("\\d{10}"));
        assertTrue(msg2.getMessageID().matches("\\d{10}"));

        // IDs should be different
        assertNotEquals(msg1.getMessageID(), msg2.getMessageID());
    }

    @Test
    public void testCreateMessageHash() {
        Message msg1 = new Message("+27831234567", "Hello World", 0);
        String hash1 = msg1.getMessageHash();

        // Should be in format: twoDigits:messageNumber:FIRSTLAST
        assertTrue(hash1.matches("\\d{2}:\\d+:\\w+"));
        assertTrue(hash1.contains(":0:HELLOWORLD"));

        Message msg2 = new Message("+27831234567", "Hi there", 1);
        String hash2 = msg2.getMessageHash();
        assertTrue(hash2.contains(":1:HITHERE"));

        Message msg3 = new Message("+27831234567", "Single", 2);
        String hash3 = msg3.getMessageHash();
        assertTrue(hash3.contains(":2:SINGLESINGLE")); // first and last word same
    }

    @Test
    public void testCreateMessageHashWithSpecialCharacters() {
        Message msg = new Message("+27831234567", "Hello, World! Testing...", 3);
        String hash = msg.getMessageHash();

        // Special characters should be removed
        assertFalse(hash.contains(","));
        assertFalse(hash.contains("!"));
        assertFalse(hash.contains("."));
        assertTrue(hash.contains("HELLOWORLD"));
    }

    @Test
    public void testStoreMessage() {
        // Create a temporary file for testing
        File testFile = new File(tempDir.toFile(), "messages.json");
        Message testMessage = new Message("+27831234567", "Test message", 0);

        // This will create the file if it doesn't exist
        testMessage.storeMessage();

        // Verify file was created (or at least no exception thrown)
        // Note: We can't easily test the actual file content without more complex setup
        assertDoesNotThrow(() -> testMessage.storeMessage());
    }

    @Test
    public void testMessageGetters() {
        assertEquals("+27831234567", message.getRecipient());
        assertEquals("Hello World", message.getMessageText());
        assertNotNull(message.getMessageHash());
        assertNotNull(message.getMessageID());
        assertFalse(message.isSent());
    }

    @Test
    public void testToString() {
        String str = message.toString();
        assertTrue(str.contains("Message ID: " + message.getMessageID()));
        assertTrue(str.contains("Message Hash: " + message.getMessageHash()));
        assertTrue(str.contains("Recipient: " + message.getRecipient()));
        assertTrue(str.contains("Message: " + message.getMessageText()));
    }
}
