import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import simpleloginsystem.Login;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class LoginTest {
    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
    }

    @Test
    public void testCheckUsernameFormatValid() {
        assertTrue(login.checkUsernameFormat("ab_c"));
        assertTrue(login.checkUsernameFormat("a_b"));
        assertTrue(login.checkUsernameFormat("user_"));
    }

    @Test
    public void testCheckUsernameFormatInvalid() {
        assertFalse(login.checkUsernameFormat("abc")); // no underscore
        assertFalse(login.checkUsernameFormat("abcdef")); // too long
        assertFalse(login.checkUsernameFormat("")); // empty
        assertFalse(login.checkUsernameFormat("abcde_")); // 6 characters
    }

    @Test
    public void testCheckPasswordComplexityValid() {
        assertTrue(login.checkPasswordComplexity("Password123!"));
        assertTrue(login.checkPasswordComplexity("Pass@1234"));
        assertTrue(login.checkPasswordComplexity("TEST123$test"));
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        assertFalse(login.checkPasswordComplexity("short")); // too short
        assertFalse(login.checkPasswordComplexity("nouppercase123!")); // no uppercase
        assertFalse(login.checkPasswordComplexity("NOLOWERCASE123!")); // no lowercase
        assertFalse(login.checkPasswordComplexity("NoNumbers!")); // no numbers
        assertFalse(login.checkPasswordComplexity("NoSpecial123")); // no special characters
    }

    @Test
    public void testCheckCellPhoneNumberFormatValid() {
        assertTrue(login.checkCellPhoneNumberFormat("+27831234567"));
        assertTrue(login.checkCellPhoneNumberFormat("+12345678901"));
        assertTrue(login.checkCellPhoneNumberFormat("+441234567890"));
    }

    @Test
    public void testCheckCellPhoneNumberFormatInvalid() {
        assertFalse(login.checkCellPhoneNumberFormat("27831234567")); // no +
        assertFalse(login.checkCellPhoneNumberFormat("+123")); // too short
        assertFalse(login.checkCellPhoneNumberFormat("+123456789012345")); // too long
        assertFalse(login.checkCellPhoneNumberFormat("+abc1234567")); // letters
        assertFalse(login.checkCellPhoneNumberFormat("")); // empty
    }

    @Test
    public void testAuthenticateUserSuccess() {
        Login testLogin = new Login("John", "Doe", "j_doe", "Pass123!", "+27831234567");
        assertTrue(testLogin.authenticateUser("j_doe", "Pass123!"));
    }

    @Test
    public void testAuthenticateUserFailure() {
        Login testLogin = new Login("John", "Doe", "j_doe", "Pass123!", "+27831234567");
        assertFalse(testLogin.authenticateUser("wrong", "Pass123!"));
        assertFalse(testLogin.authenticateUser("j_doe", "wrong"));
        assertFalse(testLogin.authenticateUser("wrong", "wrong"));
    }

    @Test
    public void testGetLoginStatusMessage() {
        Login testLogin = new Login("John", "Doe", "j_doe", "Pass123!", "+27831234567");

        String successMessage = testLogin.getLoginStatusMessage(true);
        assertEquals("Welcome John, Doe it is great to see you again.", successMessage);

        String failureMessage = testLogin.getLoginStatusMessage(false);
        assertEquals("Username or password incorrect, please try again.", failureMessage);
    }

    @Test
    public void testRegisterNewUserValid() {
        String input = "John\nDoe\nj_doe\nPass123!\n+27831234567\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        String result = login.registerNewUser(scanner);
        assertEquals("Registration successful!", result);

        System.setIn(System.in);
    }

    @Test
    public void testGetters() {
        Login testLogin = new Login("John", "Doe", "j_doe", "Pass123!", "+27831234567");

        assertEquals("John", testLogin.getFirstName());
        assertEquals("Doe", testLogin.getLastName());
        assertEquals("j_doe", testLogin.getUserName());
        assertEquals("+27831234567", testLogin.getCellNumber());
    }
}