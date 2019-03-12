package hu.pemik.poseidon.authentication;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class Authentication {
    public static final int OK = 1;
    public static final int INCORRECT_USER_NAME_OR_PASSWORD = -1;
    public static final int INCOMPLETE_INPUT = 0;
    public static final int NAME_ALREADY_TAKEN = -2;
    public static final int DIFFERENT_PASSWORDS = -3;
    public static final int DATABASE_ERROR = -10;

    public void register(String username, String password, String verifyPassword, Consumer<Integer> callback) {
        AsyncRegister asyncRegister = new AsyncRegister(callback);
        asyncRegister.execute(username, password, verifyPassword);
    }

    public void login(String username, String password, Consumer<Integer> callback) {
        AsyncLogin asyncLogin = new AsyncLogin(callback);
        asyncLogin.execute(username, password);
    }
}
