package hu.pemik.poseidon.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import hu.pemik.poseidon.R;

public class LoginDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_demo);

        okRegister();
    }

    private void okRegister() {
        Authentication authentication = new Authentication();
        TextView textView = findViewById(R.id.textView);
        authentication.register("newName", "mypassword",
                "mypassword", (Integer result) -> {
                    textView.setText(result.toString());
                });
    }

    private void nameTaken() {
        Authentication authentication = new Authentication();
        TextView textView = findViewById(R.id.textView);
        authentication.register("D3OSIL", "mypassword",
                "mypassword", (Integer result) -> {
                    textView.setText(result.toString());
                });
    }

    private void wrongVerifyPassword() {
        Authentication authentication = new Authentication();
        TextView textView = findViewById(R.id.textView);
        authentication.register("myusername", "mypassword",
                "otherpassword", (Integer result) -> {
            textView.setText(result.toString());
        });

    }

    private void incompleteLogin() {
        Authentication authentication = new Authentication();
        TextView textView = findViewById(R.id.textView);
        authentication.login("Hello", "", (Integer result) -> {
            textView.setText(result.toString());
        });
    }
}
