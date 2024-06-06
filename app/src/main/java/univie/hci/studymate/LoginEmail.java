package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class LoginEmail extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button submitLoginButton;
    private ConstraintLayout mainLayout;

    private int currentBackgroundIndex = 0;
    private int[] backgroundResources = {
            R.drawable.background_gradient,
            R.drawable.background_gradient_other,
            R.drawable.background_gradient_green,
            R.drawable.background_gradient_wine_red,
            R.drawable.background_gradient_second,
            R.drawable.background_gradient_third
    };

    private int[] textColors = {
            R.color.text_color1,
            R.color.text_color2,
            R.color.text_color3,
            R.color.text_color4,
            R.color.text_color5,
            R.color.text_color6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        submitLoginButton = findViewById(R.id.submitLoginButton);
        mainLayout = findViewById(R.id.main_layout);

        submitLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (checkInputs(email, password)) {
                    Intent intent = new Intent(LoginEmail.this, MatchingAlgorithm.class);
                    startActivity(intent);
                } else {
                    emailEditText.setError("Email can't be empty");
                    passwordEditText.setError("Password can't be empty");
                }
            }
        });

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();
    }

    private boolean checkInputs(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);

        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        emailEditText.setTextColor(textColor);
        passwordEditText.setTextColor(textColor);
        submitLoginButton.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void startSearch(User user) {
        Intent intent = new Intent(LoginEmail.this, MatchingAlgorithm.class);
        intent.putExtra(MainActivity.USER_MATCHING_ALGO_STRING, user);
        startActivity(intent);
    }
}
