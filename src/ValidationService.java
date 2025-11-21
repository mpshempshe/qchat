package simpleloginsystem;

public class ValidationService {

    public boolean checkUsername(String username){
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPassword(String password){
        if(password.length() < 8) return false;

        boolean hasCapital = false, hasNumber = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if(!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellNumber(String cellNumber){
        if (cellNumber.startsWith("+") && cellNumber.length() >= 11 && cellNumber.length() <= 13){
            for (int i = 1; i < cellNumber.length(); i++){
                if(!Character.isDigit(cellNumber.charAt(i))) return false;
            }
            return true;
        }
        return false;
    }
}