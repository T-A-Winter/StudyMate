package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginEmail extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button submitLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        submitLoginButton = findViewById(R.id.submitLoginButton);

        submitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prüft ob daten gültig sind bzw. felder leer sind "validateCredentials
                if (checkInputs(emailEditText.getText().toString(), passwordEditText.getText().toString())) {
                    Intent intent = new Intent(LoginEmail.this, MatchingAlgorithm.class);
                    startActivity(intent);
                } else {
                    emailEditText.setError("Email cant be empty");
                    passwordEditText.setError("Password cant be empty");
                }
            }
        });
    }

    //immer richtig ausser email und pw felder leer
    private boolean checkInputs(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}



