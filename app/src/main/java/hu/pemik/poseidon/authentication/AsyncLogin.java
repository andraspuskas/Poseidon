package hu.pemik.poseidon.authentication;

import android.os.AsyncTask;

import java.util.function.Consumer;

import static hu.pemik.poseidon.authentication.Authentication.*;

public class AsyncLogin extends AsyncTask<String, Void, Integer> {

    private final Consumer<Integer> callback;

    public AsyncLogin(Consumer<Integer> callback) {
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        if (username.equals("") || password.equals("")) {
            return INCOMPLETE_INPUT;
        } else {

        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        callback.accept(result);
    }
}
