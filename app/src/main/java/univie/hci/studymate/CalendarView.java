package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

//TODO: I need to implement send a meeting vote
//it


public class CalendarView extends AppCompatActivity {
    private Calendar selectedCalendar, selectedCalendar2, selectedCalendar3;
    private TimePicker timePickerStart, timePickerEnd, timePickerVote;
    private EditText TitleText, TitleTextVote;
    private AppDatabase CreatedEvents;
    Animation fadeOut, fadeIn;
    private Button newEventButton, GoBackButton, CreateEventButton, GoBackVoteButton, SendVoteButton, CreateEventVoteButton, AddTimeSlotVoteButton;
    private LinearLayout CalendarLayout, ButtonsLayout, tagsContainer;
    private ScrollView EventsScroll, PopUp, PopUpVote;
    private android.widget.CalendarView ActualCalendar, popupCalendar,popupCalendarVote;
    private ImageView settingsButton;
    private User user;
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private ConstraintLayout mainLayout;
    private int currentBackgroundIndex = 0;
    private NavBar navBar;
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
        initializeDataFetch();
    }

    private void initialization(){
        //user
        user = getUserFromIntent();
        // setting up NavBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        navBar = new NavBar(this, bottomNavigationView, user);
        //calendars
        selectedCalendar3 = Calendar.getInstance();
        selectedCalendar2 = Calendar.getInstance();
        selectedCalendar = Calendar.getInstance();
        //calendar views
        ActualCalendar  = findViewById(R.id.actual_calendar);
        popupCalendar = findViewById(R.id.popup_calendar);
        popupCalendarVote = findViewById(R.id.popup_calendar_vote);
        //time pickers
        timePickerVote = findViewById(R.id.timePickerStart_vote);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        //edit text
        TitleText = findViewById(R.id.EventTitle);
        TitleTextVote = findViewById(R.id.EventTitle_vote);
        //buttons
        AddTimeSlotVoteButton = findViewById(R.id.Add_time_slot);
        CreateEventButton = findViewById(R.id.CreateEvent);
        CreateEventVoteButton = findViewById(R.id.CreateEvent_vote);
        SendVoteButton = findViewById(R.id.send_vote);
        newEventButton = findViewById(R.id.for_yourself);
        settingsButton = findViewById(R.id.settingsButton);
        GoBackButton = findViewById(R.id.GoBack);
        GoBackVoteButton = findViewById(R.id.GoBack_vote);
        //database
        CreatedEvents = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "event-database-3").build();
        //background
        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        //layouts or scrolls
        ButtonsLayout = findViewById(R.id.buttons_layout);
        mainLayout = findViewById(R.id.main_layout);
        CalendarLayout = findViewById(R.id.calendar_layout);
        EventsScroll = findViewById(R.id.EventsScroll);
        PopUp = findViewById(R.id.scroll_pop_up);
        PopUpVote = findViewById(R.id.scroll_pop_up_vote);
        tagsContainer = findViewById(R.id.tagsContainerLayout);
        //animation
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

        //opens Send Vote PopUp
        SendVoteButton.setOnClickListener(v -> {
            CalendarLayout.startAnimation(fadeOut);
            EventsScroll.startAnimation(fadeOut);
            ButtonsLayout.startAnimation(fadeOut);
            CalendarLayout.setVisibility(View.GONE);
            EventsScroll.setVisibility(View.GONE);
            ButtonsLayout.setVisibility(View.GONE);
            PopUpVote.startAnimation(fadeIn);
            PopUpVote.setVisibility(View.VISIBLE);
        });

        //Goes back to Calendar screen from personal event
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

        //Goes back to Calendar screen from Vote Event
        GoBackVoteButton.setOnClickListener(v -> {
            PopUpVote.startAnimation(fadeOut);
            PopUpVote.setVisibility(View.GONE);
            CalendarLayout.startAnimation(fadeIn);
            EventsScroll.startAnimation(fadeIn);
            ButtonsLayout.startAnimation(fadeIn);
            CalendarLayout.setVisibility(View.VISIBLE);
            EventsScroll.setVisibility(View.VISIBLE);
            ButtonsLayout.setVisibility(View.VISIBLE);
            tagsContainer.removeAllViews();
        });

        //Goes to the next class Settings
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(univie.hci.studymate.CalendarView.this, SettingsActivity.class);
            intent.putExtra(USER_MATCHING_ALGO_STRING, user);
            startActivity(intent);
        });

        //to be honest, I don't remember, what it does, or why exactly I need it,
        //but otherwise the event date is always == current date
        popupCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> selectedCalendar.set(year, month, dayOfMonth));

        //just copied the above for the second pop-up
        popupCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> selectedCalendar3.set(year, month, dayOfMonth));

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

        //adds a time slot in a vote pop up
        AddTimeSlotVoteButton.setOnClickListener(v -> {
            LinearLayout tagLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.tag_layout, null);

            // Find TextViews inside the custom layout
            TextView timeTextView = tagLayout.findViewById(R.id.tagText);
            ImageButton close = tagLayout.findViewById(R.id.closeButton);

            int selectedHour = timePickerVote.getHour();
            int selectedMinute = timePickerVote.getMinute();
            String tagText = String.format("%02d:%02d - %02d:%02d", selectedHour, selectedMinute, selectedHour + 1, selectedMinute);
            timeTextView.setText(tagText);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeTag(tagLayout);
                }
            });

            View spacerView = new View(this);
            LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // Width of the spacer, set to match parent width
                    16 // Height of the spacer (adjust as needed for desired vertical space)
            );
            spacerView.setLayoutParams(spacerParams);

            // Add the tag view to the tags container
            tagsContainer.addView(tagLayout);

            // Add the spacer view to the tags container
            tagsContainer.addView(spacerView);});

        //sends timeslots to the chat
        CreateEventVoteButton.setOnClickListener(v -> {
            String title = TitleTextVote.getText().toString();

            String selectedDateString = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                    selectedCalendar.get(Calendar.YEAR),
                    selectedCalendar.get(Calendar.MONTH) + 1,
                    selectedCalendar.get(Calendar.DAY_OF_MONTH));

            ArrayList<String> timeSlots = new ArrayList<>();
            for (int i = 0; i < tagsContainer.getChildCount(); i++) {
                View view = tagsContainer.getChildAt(i);
                if (view instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    View view2 = viewGroup.getChildAt(0);
                    if (view2 instanceof TextView) {
                        timeSlots.add(((TextView) view2).getText().toString());
                    }
                }

            }

            Intent intent = new Intent(CalendarView.this, ChatListActivity.class);
            intent.putStringArrayListExtra("timeSlots", timeSlots);
            intent.putExtra("title", title);
            intent.putExtra("date", selectedDateString);
            startActivity(intent);
        });

        //shows events planned for the day
        ActualCalendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Create a Calendar instance and set it to the selected date
            selectedCalendar2.set(year, month, dayOfMonth);


            // Convert the selected date to a string in the format "yyyy-MM-dd"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String selectedDateString = sdf.format(selectedCalendar2.getTime());

            Executors.newSingleThreadExecutor().execute(() -> {
                // Query the database for events on the selected date
                List<EventEntity> events = CreatedEvents.eventDao().getEventsByDate(selectedDateString);
                // Update the UI with the retrieved events
                runOnUiThread(() -> populateEvents(events));
            });
        });
    }

    private void populateEvents(List<EventEntity> events) {
        LinearLayout eventsLayout = findViewById(R.id.EventsScroll).findViewById(R.id.allEvents);
        eventsLayout.removeAllViews(); // Clear the existing views

        if (events.isEmpty()) {
            // Show a message if there are no events for the selected date
            LinearLayout noIvents = (LinearLayout) getLayoutInflater().inflate(R.layout.no_events_message, null);
            eventsLayout.addView(noIvents);
        } else {
            // Populate the scroll section with events
            for (EventEntity event : events) {
                LinearLayout eventLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.event_layout, null);

                // Find TextViews inside the custom layout
                TextView timeTextView = eventLayout.findViewById(R.id.timeTextView);
                TextView titleTextView = eventLayout.findViewById(R.id.titleTextView);

                // Set the time and title for each TextView
                timeTextView.setText(String.format("%s - %s", event.getStartTime(), event.getEndTime()));

                if(!TextUtils.isEmpty(event.getTitle())){
                    titleTextView.setText(event.getTitle());
                }else {
                    titleTextView.setVisibility(View.GONE);
                }

                // Add the custom layout for the event to the eventsLayout
                eventsLayout.addView(eventLayout);
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
        popupCalendarVote.setMinDate(today);

        //restriction for time
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        timePickerStart.setCurrentHour(currentHour);
        timePickerStart.setCurrentMinute(currentMinute);
        timePickerEnd.setCurrentHour(currentHour);
        timePickerEnd.setCurrentMinute(currentMinute);

        timePickerStart.setOnTimeChangedListener((view, startHour, startMinute) -> {
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
        });

        timePickerEnd.setOnTimeChangedListener((view, startHour, startMinute) -> {
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
        });

    }

    private boolean isSameDate(Calendar c1, Calendar c2){
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)&&
            c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)&&
            c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)){
            return true;
        }
        return false;
    }

    private void initializeDataFetch(){

        // Convert the selected date to a string in the format "yyyy-MM-dd"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String selectedDateString = sdf.format(selectedCalendar2.getTime());

        Executors.newSingleThreadExecutor().execute(() -> {
            // Query the database for events on the selected date
            List<EventEntity> events = CreatedEvents.eventDao().getEventsByDate(selectedDateString);
            // Update the UI with the retrieved events
            runOnUiThread(() -> populateEvents(events));
        });
    }

    private void removeTag(View tagView) {
        tagsContainer.removeView(tagView);
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
