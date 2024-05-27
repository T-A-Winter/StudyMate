package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executors;

//TODO: I need to implement send a meeting vote + restrictions for creation of an event
//TODO: because now you can add an event that starts later than it Ends or happens in the past
//Firstly, I don't really understand how to
//Secondly, For that I will need to see Chat implementation, which
//at the moment is absent

public class CalendarView extends AppCompatActivity {
    private Calendar selectedCalendar;
    private TimePicker timePickerStart, timePickerEnd;
    private EditText TitleText;
    private AppDatabase CreatedEvents;
    Animation fadeOut, fadeIn;
    private Button newEventButton, GoBackButton, CreateEventButton;
    private LinearLayout CalendarLayout, ButtonsLayout;
    private ScrollView EventsScroll, PopUp;
    private android.widget.CalendarView ActualCalendar, popupCalendar;
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
        applyBackground();
        setListeners();
        applyRestrictions();

    }

    private void initialization(){
        selectedCalendar = Calendar.getInstance();
        ActualCalendar  = findViewById(R.id.actual_calendar);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        popupCalendar = findViewById(R.id.popup_calendar);
        TitleText = findViewById(R.id.EventTitle);selectedCalendar = Calendar.getInstance();
        ActualCalendar  = findViewById(R.id.actual_calendar);
        CreateEventButton = findViewById(R.id.CreateEvent);
        CreatedEvents = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "event-database-3").build();
        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        settingsButton = findViewById(R.id.settingsButton);
        user = getUserFromIntent();
        mainLayout = findViewById(R.id.main_layout);
        CalendarLayout = findViewById(R.id.calendar_layout);
        EventsScroll = findViewById(R.id.EventsScroll);
        ButtonsLayout = findViewById(R.id.buttons_layout);
        newEventButton = findViewById(R.id.for_yourself);
        PopUp = findViewById(R.id.scroll_pop_up);
        GoBackButton = findViewById(R.id.GoBack);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
    }

    private void setListeners(){
        //opens New Event PopUp
        newEventButton.setOnClickListener(v -> {
            CalendarLayout.startAnimation(fadeOut);
            EventsScroll.startAnimation(fadeOut);
            ButtonsLayout.startAnimation(fadeOut);
            CalendarLayout.setVisibility(View.GONE);
            EventsScroll.setVisibility(View.GONE);
            ButtonsLayout.setVisibility(View.GONE);
            PopUp.startAnimation(fadeIn);
            PopUp.setVisibility(View.VISIBLE);
        });

        //Goes back to Calendar screen
        GoBackButton.setOnClickListener(v -> {
            PopUp.startAnimation(fadeOut);
            PopUp.setVisibility(View.GONE);
            CalendarLayout.startAnimation(fadeIn);
            EventsScroll.startAnimation(fadeIn);
            ButtonsLayout.startAnimation(fadeIn);
            CalendarLayout.setVisibility(View.VISIBLE);
            EventsScroll.setVisibility(View.VISIBLE);
            ButtonsLayout.setVisibility(View.VISIBLE);
        });

        //Goes to the next class Settings
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(univie.hci.studymate.CalendarView.this, SettingsActivity.class);
            intent.putExtra(USER_MATCHING_ALGO_STRING, user);
            startActivity(intent);
        });

        popupCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedCalendar.set(year, month, dayOfMonth);
        });

        //saves a created Event in Database stored locally on a device (not connected to the user account)
        //In future can be used to temporary store events while offline
        CreateEventButton.setOnClickListener(v -> {

            //additional timing check/change
            if (isSameDate(selectedCalendar, Calendar.getInstance())){
                if(timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ||
                        (timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)&&  timePickerStart.getMinute()< Calendar.getInstance().get(Calendar.MINUTE))){
                    timePickerStart.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    timePickerStart.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
                }
            }

            if (timePickerStart.getHour() > timePickerEnd.getHour() || (timePickerStart.getHour() == timePickerEnd.getHour() && timePickerStart.getMinute() > timePickerEnd.getMinute())) {
                timePickerEnd.setCurrentHour(timePickerStart.getHour());
                timePickerEnd.setCurrentMinute(timePickerStart.getMinute());
            }

            String title = TitleText.getText().toString();

            int startHour = timePickerStart.getHour();
            int startMinute = timePickerStart.getMinute();
            int endHour = timePickerEnd.getHour();
            int endMinute = timePickerEnd.getMinute();

            // Set the selected time to the selectedCalendar
            selectedCalendar.set(Calendar.HOUR_OF_DAY, startHour);
            selectedCalendar.set(Calendar.MINUTE, startMinute);

            Calendar endTime = (Calendar) selectedCalendar.clone();
            endTime.set(Calendar.HOUR_OF_DAY, endHour);
            endTime.set(Calendar.MINUTE, endMinute);

            String selectedDateString = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH) + 1,
                    selectedCalendar.get(Calendar.DAY_OF_MONTH));

            String startTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedCalendar.getTime());
            String endTimeString = DateFormat.getTimeInstance(DateFormat.SHORT).format(endTime.getTime());

            // Hide PopUp and show other layouts with animation
            PopUp.startAnimation(fadeOut);
            PopUp.setVisibility(View.GONE);
            CalendarLayout.startAnimation(fadeIn);
            EventsScroll.startAnimation(fadeIn);
            ButtonsLayout.startAnimation(fadeIn);
            CalendarLayout.setVisibility(View.VISIBLE);
            EventsScroll.setVisibility(View.VISIBLE);
            ButtonsLayout.setVisibility(View.VISIBLE);

            EventEntity event = new EventEntity(title, startTimeString, endTimeString, selectedDateString);
            insertEvent(event);
        });

        ActualCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Create a Calendar instance and set it to the selected date
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);

            // Convert the selected date to a string in the format "yyyy-MM-dd"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDateString = sdf.format(selectedDate.getTime());

            Executors.newSingleThreadExecutor().execute(() -> {
                // Query the database for events on the selected date
                List<EventEntity> events = CreatedEvents.eventDao().getEventsByDate(selectedDateString);
                // Update the UI with the retrieved events
                runOnUiThread(() -> populateEvents(events));
            });
        });
    }

    //TODO: Make it look at least somewhat user friendly
    private void populateEvents(List<EventEntity> events) {
        LinearLayout eventsLayout = findViewById(R.id.EventsScroll).findViewById(R.id.EventScrollLinearLayout);
        eventsLayout.removeAllViews(); // Clear the existing views

        if (events.isEmpty()) {
            // Show a message if there are no events for the selected date
            TextView noEventsText = findViewById(R.id.NoIventsText);
            noEventsText.setVisibility(View.VISIBLE);
        } else {
            // Populate the scroll section with events
            for (EventEntity event : events) {
                TextView eventTextView = new TextView(this);
                eventTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                eventTextView.setText(String.format("%s - %s - %s", event.getTitle(), event.getStartTime(),event.getEndTime()));
                eventsLayout.addView(eventTextView);
            }
        }
    }
    private void insertEvent(EventEntity event) {
        new Thread(() -> CreatedEvents.eventDao().insert(event)).start();
    }

    //to be honest there are a lot of listeners as well
    private void applyRestrictions() {

        //restriction for popup calendar
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        popupCalendar.setMinDate(today);

        //restriction for time
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        timePickerStart.setCurrentHour(currentHour);
        timePickerStart.setCurrentMinute(currentMinute);
        timePickerEnd.setCurrentHour(currentHour);
        timePickerEnd.setCurrentMinute(currentMinute);

        timePickerStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int startHour, int startMinute) {
                int finishHour = timePickerEnd.getCurrentHour();
                int finishMinute = timePickerEnd.getCurrentMinute();

                if (isSameDate(selectedCalendar, Calendar.getInstance())){
                    if(timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ||
                            (timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)&&  timePickerStart.getMinute()< Calendar.getInstance().get(Calendar.MINUTE))){
                        timePickerStart.setCurrentHour(startHour);
                        timePickerStart.setCurrentMinute(startMinute);
                    }
                }

                if (startHour > finishHour || (startHour == finishHour && startMinute > finishMinute)) {
                    timePickerEnd.setCurrentHour(startHour);
                    timePickerEnd.setCurrentMinute(startMinute);
                }
            }
        });

        timePickerEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int startHour, int startMinute) {
                int finishHour = timePickerEnd.getCurrentHour();
                int finishMinute = timePickerEnd.getCurrentMinute();

                if (isSameDate(selectedCalendar, Calendar.getInstance())){
                    if(timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY) ||
                            (timePickerStart.getHour() < Calendar.getInstance().get(Calendar.HOUR_OF_DAY)&&  timePickerStart.getMinute()< Calendar.getInstance().get(Calendar.MINUTE))){
                        timePickerStart.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        timePickerStart.setCurrentMinute(Calendar.getInstance().get(Calendar.MINUTE));
                    }
                }

                if (startHour > finishHour || (startHour == finishHour && startMinute > finishMinute)) {
                    timePickerEnd.setCurrentHour(startHour);
                    timePickerEnd.setCurrentMinute(startMinute);
                }
            }
        });

    };

    private boolean isSameDate(Calendar c1, Calendar c2){
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)&&
            c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)&&
            c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)){
            return true;
        }
        return false;
    };

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
