package model;

public class Validations {
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }

    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=^.{8,45}$)(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9]).*$");
    }

    public static boolean isInt(String num) {
        return num.matches("^[1-9]\\d*$");
    }

    public static boolean isDouble(String num) {
        return num.matches("^[+-]?([0-9]*[.])?[0-9]+$");
    }
    
    public static boolean isMobile(String  mobile){
        return mobile.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$");
    }
}
