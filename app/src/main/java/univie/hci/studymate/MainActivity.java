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

    private int currentBackgroundIndex = 0;
    // background drawable resources array,  4 colors
    private int[] backgroundResources = {
            R.drawable.background_gradient,
            R.drawable.background_gradient_other,
            R.drawable.background_gradient_second,
            R.drawable.background_gradient_third

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginEmailButton = findViewById(R.id.loginEmailButton);
        changeBackgroundButton = findViewById(R.id.changeBackgroundButton);

        applyBackground();

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


    //change button click
        changeBackgroundButton.setOnClickListener(v -> {
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgroundResources.length;
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putInt("backgroundIndex", currentBackgroundIndex).apply();
            updateBackground();
        });

        // last saved background
        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        updateBackground();
    }
    //update background drawable
    private void updateBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
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
    //apply this saved background
    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);
    }
}
