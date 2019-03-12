package hu.pemik.poseidon.authentication;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import static hu.pemik.poseidon.authentication.Authentication.*;

public class AsyncRegister extends AsyncTask<String, Void, Integer> {

    private final Consumer<Integer> callback;

    public AsyncRegister(Consumer<Integer> callback) {
        this.callback = callback;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        String verifyPassword = strings[2];
        if (username.equals("") || password.equals("") || verifyPassword.equals("")) {
            return INCOMPLETE_INPUT;
        } else if (!password.equals(verifyPassword)) {
            return DIFFERENT_PASSWORDS;
        } else {
            try {
                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
                String UrlString =
                        "jdbc:jtds:sqlserver://193.6.33.140:1433/PoseidonDb;user=poseidon;password=Poseidon_1";

                Connection conn = DriverManager.getConnection(UrlString);
                if (conn == null) {
                    return DATABASE_ERROR;
                } else {
                    String sql =    "SELECT COUNT(*) as count " +
                                    "FROM bejelentkezes " +
                                    "WHERE felhasznalo = " + "'" + username + "'";
                    Statement statement = conn.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    resultSet.next();
                    int count = resultSet.getInt("count");

                    if (count > 0) {
                        conn.close();
                        return NAME_ALREADY_TAKEN;
                    }

                    sql = "INSERT INTO bejelentkezes\n" +
                            "VALUES ('" + username + "', '" + password + "')";
                    statement.executeUpdate(sql);
                    conn.close();
                    return OK;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return DATABASE_ERROR;
            }
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        callback.accept(result);
    }
}
