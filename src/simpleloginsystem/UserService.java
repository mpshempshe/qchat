package simpleloginsystem;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private List<User> users;
    private ValidationService validationService;

    public UserService() {
        this.users = new ArrayList<>();
        this.validationService = new ValidationService();
        // Create sample user as per POE requirements
        createSampleUser();
    }

    private void createSampleUser() {
        User sampleUser = new User("Keegan", "Miller", "k_mil", "Pass123!", "+27838884567");
        users.add(sampleUser);
    }

    public boolean registerUser(User user) {
        if (validationService.checkUsername(user.getUsername()) &&
                validationService.checkPassword(user.getPassword()) &&
                validationService.checkCellNumber(user.getCellNumber())) {
            users.add(user);
            return true;
        }
        return false;
    }

    public User loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public boolean usernameExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}