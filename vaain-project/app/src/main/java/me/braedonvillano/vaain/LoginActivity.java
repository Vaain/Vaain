package me.braedonvillano.vaain;

import android.app.ActionBar;
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

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getActionBar();
        actionBar.hide();


        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnlogin);
        signupBtn = findViewById(R.id.btnSign);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                login(username, password);

                }
            });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                }
            });
        }

        private void login(String username, String password) {
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null) {
                       startUserActivity(user.getBoolean("isClient"));
                    } else {
                        Log.e("LoginActivity", "Login failure");
                        e.printStackTrace();
                    }
                }
            });
        }

        public void startUserActivity(Boolean isClient) {
            final Intent intent;
            if (isClient) {
                intent = new Intent(LoginActivity.this, MainActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, BeautMainActivity.class);
            }
            startActivity(intent);
            finish();
        }
    }

