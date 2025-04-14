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

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = Objects.requireNonNull(MainActivity.class.getPackage()).toString();

    private EditText emailET;
    private EditText userPasswordET;

    private SharedPreferences preferences;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailET = findViewById(R.id.editTextUserEmail);
        userPasswordET = findViewById(R.id.editTextUserPassword);
        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Saving username and password
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userEmail", emailET.getText().toString());
        editor.putString("password", userPasswordET.getText().toString());
        editor.apply();

    }

    public void login(View view) {
        String userName = emailET.getText().toString();
        String userPassword = userPasswordET.getText().toString();

        if(!userName.isEmpty() && !userPassword.isEmpty()){
            mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    startYourNotesActivity();
                } else{
                    Log.i(LOG_TAG,"LOGIN FAILED");
                }
            });
        }
    }

    private void startYourNotesActivity() {
        Intent intent = new Intent(this, YourNotesActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

    public void signUp(View view) {
        startRegistrationActivity();
    }

    private void startRegistrationActivity(){
        Intent startRegistration = new Intent(this, RegisterActivity.class);
        startActivity(startRegistration);
    }
}