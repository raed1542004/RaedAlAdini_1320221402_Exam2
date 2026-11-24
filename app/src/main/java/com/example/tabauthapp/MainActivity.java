package com.example.tabauthapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText emailField;
    private EditText passField;
    private Button loginBtn;
    private long lastBackTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailField = findViewById(R.id.et_email);
        passField = findViewById(R.id.et_password);
        loginBtn = findViewById(R.id.btn_login);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        emailField.addTextChangedListener(watcher);
        passField.addTextChangedListener(watcher);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ModeActivity.class));
            }
        });
    }

    private void validateInputs() {
        String email = emailField.getText() != null ? emailField.getText().toString().trim() : "";
        String pass = passField.getText() != null ? passField.getText().toString() : "";

        boolean validEmail = !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean validPass = pass.length() >= 6;

        loginBtn.setEnabled(validEmail && validPass);
    }

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - lastBackTime < 2000) {
            finishAffinity();
            return;
        }
        lastBackTime = now;
        Snackbar.make(findViewById(R.id.main_root), R.string.exit_prompt, Snackbar.LENGTH_SHORT).show();
    }
}
