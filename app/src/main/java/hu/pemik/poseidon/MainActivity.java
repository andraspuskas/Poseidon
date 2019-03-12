package hu.pemik.poseidon;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doNetwork();
    }

    private void doNetwork() {
        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
                String UrlString = "jdbc:jtds:sqlserver://193.6.33.140:1433/PoseidonDb;user=poseidon;password=Poseidon_1";

                Connection conn = DriverManager.getConnection(
                        UrlString);
                if (conn != null) {
                    System.out.println("Connected");
                } else {
                    System.out.println("Not connected");
                    return null;
                }

                String sql = "select * from targy";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    //System.out.println(rs.);
                    System.out.println(resultSet.getString("kod") + " \t" + resultSet.getString("nev"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
