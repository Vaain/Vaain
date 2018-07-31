package me.braedonvillano.vaain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    EditText password;
    EditText email;
    EditText phonenum;
    EditText name;
    Button register;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        password = (EditText) findViewById(R.id.etEnterPass);
        email = (EditText) findViewById(R.id.etEnterEmail);
        phonenum = (EditText) findViewById(R.id.etEnterPhone);
        register = (Button) findViewById(R.id.btnSignUp);
        name = (EditText) findViewById(R.id.etEnterName);
        user = new ParseUser();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the ParseUser

                // Set core properties
                user.setPassword(password.getText().toString());
                user.setEmail(email.getText().toString());
                user.setUsername(email.getText().toString());
                user.put("Name",name.getText().toString());
                // Set custom properties
                user.put("phone", phonenum.getText().toString());


                // logincallback so we know when network request was completed
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.d("SignUpActivity", "Sign Up successful");
                            startUserActivity(user.getBoolean("isClient"));
                        } else {
                            Log.e("SignUpActivity", "Sign Up failure");
                            e.printStackTrace();
                        }
                    }
                });


            }
        });


    }

    public void onCheckboxClicked(View view) {

        CheckBox clientCheckBox = (CheckBox) findViewById(R.id.cbClient);
        CheckBox beautCheckBox = (CheckBox) findViewById(R.id.cbBeaut);

        switch (view.getId()) {
            case R.id.cbClient:
                if (clientCheckBox.isChecked()) {
                    user.put("isClient", true);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("SignUpActivity", "Saved isClient to true");
                        }
                    });
                }
            case R.id.cbBeaut:
                if (beautCheckBox.isChecked()) {
                    user.put("isClient", false);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.d("SignUpActivity", "Saved isClient to false");
                        }
                    });
                } else
                    break;
        }
    }

    public void startUserActivity(Boolean isClient) {
        final Intent intent;
        if (isClient) {
            intent = new Intent(SignUpActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SignUpActivity.this, BeautMainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}

