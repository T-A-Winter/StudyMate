package univie.hci.studymate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SettingsActivity extends AppCompatActivity {

    private ImageView chatButton , matchingButton , calendarButton, friendsListButton;
    private User user;

    private ConstraintLayout mainLayout;
    private ImageView changeBackgroundButton;
    private boolean isOldBackground;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        mainLayout = findViewById(R.id.main_layout);
        changeBackgroundButton = findViewById(R.id.changeBackgroundButton);

        isOldBackground = getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("isOldBackground", true);
        updateBackground();

        changeBackgroundButton.setOnClickListener(v -> {
            isOldBackground = !isOldBackground;
            getSharedPreferences("prefs", MODE_PRIVATE).edit().putBoolean("isOldBackground", isOldBackground).apply();
            updateBackground();
        });





        // getting user
        user = getUserFromIntent();
        // TODO: When the other views are finished, the user needs to be send to the other
        // TODO: views. Otherwise, user object is lost.
        chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, Chat.class);
            startActivity(intent);
        });

        calendarButton = findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MatchingAlgorithm.class);
            startActivity(intent);
        });

        matchingButton = findViewById(R.id.matchingButton);
        matchingButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MatchingAlgorithm.class);
            startActivity(intent);
        });

        friendsListButton = findViewById(R.id.friendsListButton);
        friendsListButton.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MatchingAlgorithm.class);
            startActivity(intent);
        });

        //dodano
        boolean isOldBackground = getSharedPreferences("prefs", MODE_PRIVATE).getBoolean("isOldBackground", true);
        Drawable background = isOldBackground ?
                ContextCompat.getDrawable(this, R.drawable.background_gradient) :
                ContextCompat.getDrawable(this, R.drawable.background_gradient_other);
        mainLayout.setBackground(background);


    }

    private void updateBackground() {
        Drawable background = isOldBackground ?
                ContextCompat.getDrawable(this, R.drawable.background_gradient) :
                ContextCompat.getDrawable(this, R.drawable.background_gradient_other);
        mainLayout.setBackground(background);
    }

    private User getUserFromIntent() {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra(MainActivity.USER_MATCHING_ALGO_STRING);
        if (user == null) {
            user = setFailSafeUser();
        }
        return user;
    }

    private User setFailSafeUser() {
        String name = "failSafeUser";
        Collection<Tag> tags = new ArrayList<>(Arrays.asList(Tag.ERSTI, Tag.HCI));
        University uni = University.UNI_WIEN;
        String email = "failsafe@example.com";
        return new User(name, uni, tags, email);
    }
}

