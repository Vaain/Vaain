package me.braedonvillano.vaain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameInput = findViewById(R.id.etUsername);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnlogin);
        signupBtn = findViewById(R.id.btnSign);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);

                }
            });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, HomeFragment.class);
                startActivity(i);
                }
            });


        }

        private void login(String username, String password) {
            // logincallback so we know when network request was completed
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                        Log.d("LoginActivity", "Login successful");
                        final Intent intent = new Intent(LoginActivity.this, HomeFragment.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("LoginActivity", "Login failure");
                        e.printStackTrace();
                    }
                }
            });
        }
    }

