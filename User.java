package simpleloginsystem;

public class User {
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;

    public User(String firstName, String lastName, String username, String password, String cellNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCellNumber() { return cellNumber; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}