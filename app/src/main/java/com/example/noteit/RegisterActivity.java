package com.example.noteit;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(RegisterActivity.class.getPackage()).toString();

    private SharedPreferences preferences;

    private EditText userNameET;
    private EditText emailAddressET;
    private EditText userPasswordET;
    private EditText userPasswordConfirmET;

  private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        userNameET = findViewById(R.id.userNameET);
        emailAddressET = findViewById(R.id.emailAddressET);
        userPasswordET = findViewById(R.id.userPasswordET);
        userPasswordConfirmET = findViewById(R.id.userPasswordConfirmET);

        //Get saved preferences from home page - email and password
        String emailAddress = preferences.getString("userEmail","");
        String password = preferences.getString("password","");

        emailAddressET.setText(emailAddress);
        userPasswordET.setText(password);
        userPasswordConfirmET.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void registration(View view) {
        String email = emailAddressET.getText().toString();
        String password = userPasswordET.getText().toString();
        String userName = userNameET.getText().toString();
        Log.i(LOG_TAG,"Email:" + email + " pass: " + password);

        //you must give username
        if(!userName.isEmpty()){
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    startYourNotesActivity();
                    Log.i(LOG_TAG,"REGISTRATION SUCCESSFUL");
                    finish();
                } else{
                    Log.i(LOG_TAG,"REGISTRATION FAILED");
                }
            });
        }
    }

    private void startYourNotesActivity() {
        Intent intent = new Intent(this, YourNotesActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Saving username for later use
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userNameET.getText().toString());
        editor.apply();
    }
}