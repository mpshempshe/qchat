package simpleloginsystem;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Login {
    private String userName;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;

    // Default Constructor
    public Login() {
        this.firstName = "";
        this.lastName = "";
        this.cellNumber = "";
        this.userName = "";
        this.password = "";
    }

    // Constructor
    public Login(String firstName, String lastName, String userName,
                 String password, String cellNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    // Check if username meets requirements
    public boolean checkUsernameFormat(String username) {
        return username.length() <= 5 && username.contains("_");
    }

    // Check if password meets complexity requirements
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        if (!Pattern.compile("[0-9]").matcher(password).find()) return false;
        return Pattern.compile("[^A-Za-z0-9]").matcher(password).find();
    }

    // Check if cell phone number is correctly formatted
    public boolean checkCellPhoneNumberFormat(String cellNumber) {
        String pattern = "^\\+\\d{1,3}\\d{7,10}$";
        return Pattern.matches(pattern, cellNumber);
    }

    // Handle user registration process
    public String registerNewUser(Scanner inputScanner) {
        System.out.print("Enter your first name: ");
        this.firstName = inputScanner.nextLine();

        System.out.print("Enter your last name: ");
        this.lastName = inputScanner.nextLine();

        System.out.print("Enter username (must contain _ and be ≤5 characters): ");
        String username = inputScanner.nextLine();
        if (!checkUsernameFormat(username)) {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        this.userName = username;
        System.out.println("Username successfully captured.");

        System.out.print("Enter password (≥8 chars, with capital, number, special char): ");
        String password = inputScanner.nextLine();
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        this.password = password;
        System.out.println("Password successfully captured.");

        System.out.print("Enter cell phone number (with international code, e.g., +27831234567): ");
        String cellNumber = inputScanner.nextLine();
        if (!checkCellPhoneNumberFormat(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        this.cellNumber = cellNumber;
        System.out.println("Cell number successfully captured.");

        return "Registration successful!";
    }

    // Verify login credentials
    public boolean authenticateUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(this.userName) && enteredPassword.equals(this.password);
    }

    // Return appropriate login status message
    public String getLoginStatusMessage(boolean isSuccessful) {
        if (isSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // Getters for user information
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getUserName() { return userName; }
    public String getCellNumber() { return cellNumber; }
}