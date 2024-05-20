package univie.hci.studymate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private ImageView chatButton , matchingButton , calendarButton, friendsListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

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


}


}

