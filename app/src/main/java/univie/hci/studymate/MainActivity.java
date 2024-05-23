package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    public static final String USER_MATCHING_ALGO_STRING = "USER_MATCHING_ALGO_STRING";
    private Button createAccountButton;
    private Button loginEmailButton;
    private ConstraintLayout mainLayout;
    private ImageView changeBackgroundButton;

    private boolean isOldBackground ; // DODATI OVO DESNO = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginEmailButton = findViewById(R.id.loginEmailButton);
        changeBackgroundButton = findViewById(R.id.changeBackgroundButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }
        });

        loginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginEmail.class);
                startActivity(intent);
            }
        });

//ovo dole komentar stari za boju backgrund
      /* changeBackgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOldBackground) {
                    Drawable altBackground = ContextCompat.getDrawable(MainActivity.this, R.drawable.background_gradient_other);
                    mainLayout.setBackground(altBackground);
                } else {
                    Drawable originalBackground = ContextCompat.getDrawable(MainActivity.this, R.drawable.background_gradient);
                    mainLayout.setBackground(originalBackground);
                }
                isOldBackground = !isOldBackground;
            }
        });*/

        changeBackgroundButton.setOnClickListener(v -> {
            isOldBackground = !isOldBackground;
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putBoolean("isOldBackground", isOldBackground).apply();
            updateBackground();
        });
    }

    private void updateBackground() {
        Drawable background = isOldBackground ?
                ContextCompat.getDrawable(this, R.drawable.background_gradient) :
                ContextCompat.getDrawable(this, R.drawable.background_gradient_other);
        mainLayout.setBackground(background);
    }



    //TODO: eventuell löschen weil man aus CreateAccount.java auf MatchingAlgorithm zugreifen kann
    private void startSearch() {
        Intent intent = new Intent(MainActivity.this, MatchingAlgorithm.class);
        // TODO: When the user is created put it in the intent as an extra
        // User user; TODO: get the infos for a new user and create the user
        // intent.putExtra(USER_MATCHING_ALGO_STRING, user);
        startActivity(intent);
    }
}
