package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    public static final String USER_MATCHING_ALGO_STRING = "USER_MATCHING_ALGO_STRING";
    private Button createAccountButton;
    private Button loginEmailButton;
    private ConstraintLayout mainLayout;
    private ImageView changeBackgroundButton;
    private TextView titleTextView;

    private int currentBackgroundIndex = 0;
    private int[] backgroundResources = {
            R.drawable.background_gradient,
            R.drawable.background_gradient_other,
            R.drawable.background_gradient_green,
            R.drawable.background_gradient_wine_red,
            R.drawable.background_gradient_second,
            R.drawable.background_gradient_third
    };

    // Corresponding text colors for each background
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
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginEmailButton = findViewById(R.id.loginEmailButton);
        changeBackgroundButton = findViewById(R.id.changeBackgroundButton);
        titleTextView = findViewById(R.id.textView);

        applyBackground();

        createAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(intent);
        });

        loginEmailButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginEmail.class);
            startActivity(intent);
        });

        changeBackgroundButton.setOnClickListener(v -> {
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundResources.length;
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putInt("backgroundIndex", currentBackgroundIndex).apply();
            updateBackground();
        });

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        updateBackground();
    }

    private void updateBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);

        // Update text color
        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        createAccountButton.setTextColor(getResources().getColor(R.color.text_color2));
        loginEmailButton.setTextColor(getResources().getColor(R.color.text_color2));
        titleTextView.setTextColor(textColor);
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);

        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        createAccountButton.setTextColor(textColor);
        loginEmailButton.setTextColor(textColor);
        titleTextView.setTextColor(textColor);
    }
}
