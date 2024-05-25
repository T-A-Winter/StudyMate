package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class CalendarView extends AppCompatActivity {
    private Button newEventButton, GoBackButton;
    private LinearLayout CalendarLayout, ButtonsLayout;
    private ScrollView EventsScroll, PopUp;
    private android.widget.CalendarView CalendarView;
    private java.util.Calendar calendar;
    private ImageView settingsButton;
    private User user;
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private ConstraintLayout mainLayout;
    private int currentBackgroundIndex = 0;
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
        setContentView(R.layout.calendar_activity);

        initialization();

        //listeners
        newEventButton.setOnClickListener(v -> {
            CalendarLayout.setVisibility(View.GONE);
            EventsScroll.setVisibility(View.GONE);
            ButtonsLayout.setVisibility(View.GONE);
            PopUp.setVisibility(View.VISIBLE);
        });

        GoBackButton.setOnClickListener(v -> {
            PopUp.setVisibility(View.GONE);
            CalendarLayout.setVisibility(View.VISIBLE);
            EventsScroll.setVisibility(View.VISIBLE);
            ButtonsLayout.setVisibility(View.VISIBLE);
        });

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();

        user = getUserFromIntent();

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(univie.hci.studymate.CalendarView.this, SettingsActivity.class);
            intent.putExtra(USER_MATCHING_ALGO_STRING, user);
            startActivity(intent);
        });

    }

    private void initialization(){
        mainLayout = findViewById(R.id.main_layout);
        CalendarLayout = findViewById(R.id.calendar_layout);
        EventsScroll = findViewById(R.id.EventsScroll);
        ButtonsLayout = findViewById(R.id.buttons_layout);
        newEventButton = findViewById(R.id.for_yourself);
        PopUp = findViewById(R.id.scroll_pop_up);
        GoBackButton = findViewById(R.id.GoBack);
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);
    }


    private User getUserFromIntent() {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra(USER_MATCHING_ALGO_STRING);
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
