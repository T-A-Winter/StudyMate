package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatchingAlgorithm extends AppCompatActivity {
    private final Random random = new Random();
    // matching algo is created with random users
    private final Matching matchingAlgo = new Matching(createRandomUsers());
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private User user;
    private NavBar navBar;
    private Queue<User> matchedUsers;
    private User currentlyViewedUser;
    private ImageView settingsButton;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matching_algorithm);

        mainLayout = findViewById(R.id.main_layout);

        // get the user
        user = getUserFromIntent();

        // sets up NavBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_matching);
        navBar = new NavBar(this, bottomNavigationView, user);

        // get matching users
        matchedUsers = new LinkedList<>(matchingAlgo.match_more(user));

        // prompt first matched user
        promptFirstUser();

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return navBar.onMenuItemClick(item) || super.onOptionsItemSelected(item);
    }

    public void settingsButton(View view) {
        Intent intent = new Intent(MatchingAlgorithm.this, SettingsActivity.class);
        intent.putExtra(USER_MATCHING_ALGO_STRING, user);
        startActivity(intent);
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

    private void setDefaultProfilePicture() {
        PhotoView profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setImageResource(R.drawable.defaul_user);
    }

    private User setUser() {
        Intent intent = getIntent();
        return intent.getParcelableExtra(USER_MATCHING_ALGO_STRING);
    }

    private Collection<User> createRandomUsers() {
        // creating the users
        return IntStream.range(1, 21)
                .mapToObj(userIndex -> {
                    String name = "User" + userIndex;
                    String email = "user" + userIndex + "@example.com";
                    Integer phoneNumber = 123456 + userIndex;
                    String bio = "bio from user" + userIndex;

                    // rand Uni
                    University randomUniversity = University.values()[random.nextInt(University.values().length)];

                    // selecting 1-3 rand Tags
                    List<Tag> tagList = random.ints(1, 4)
                            .mapToObj(n -> Tag.values()[random.nextInt(Tag.values().length)])
                            .distinct()
                            .limit(3)
                            .collect(Collectors.toList());

                    return new User(name, randomUniversity, tagList, email, phoneNumber, bio);
                })
                .collect(Collectors.toList());
    }

    private void promptFirstUser() {
        pollUsers();
        promptMatchedUser();
    }

    public void matchedWithUser(View view) {
        // Add the matched user to the friend list
        FriendList.getInstance(getApplicationContext()).addFriend(currentlyViewedUser);
        pollUsers();
        promptMatchedUser();
    }

    public void declineWithUser(View view) {
        pollUsers();
        promptMatchedUser();
    }

    private void pollUsers() {
        currentlyViewedUser = matchedUsers.poll();
        if (currentlyViewedUser == null) {
            matchedUsers.add(setFailSafeUser());
            currentlyViewedUser = matchedUsers.poll();
        }
    }

    private void promptMatchedUser() {
        TextView uniTextView = findViewById(R.id.UniTextView);
        TextView tagsTextView = findViewById(R.id.TagsTextView);
        TextView bioTextView = findViewById(R.id.BioTextView);
        TextView nameTextView = findViewById(R.id.nameTextView);

        String nothingHereText = "Sorry nothing here";
        if (currentlyViewedUser == null) {
            uniTextView.setText(nothingHereText);
            tagsTextView.setText(nothingHereText);
            bioTextView.setText(nothingHereText);
            nameTextView.setText(nothingHereText);
            return;
        }

        // get the strings for the TextViews
        nameTextView.setText(currentlyViewedUser.getName());
        String uniTextPrefix = "Uni: ";
        String uniText = uniTextPrefix + currentlyViewedUser.getUniversity().name();
        uniTextView.setText(uniText);

        String tags = currentlyViewedUser.getTags().stream()
                .map(Tag::name)
                .collect(Collectors.joining(", "));

        String tagsTextPrefix = "Tags: ";
        if (tags.isEmpty()) {
            String nothingHereTags = tagsTextPrefix + nothingHereText;
            tagsTextView.setText(nothingHereTags);
        } else {
            String tagsText = tagsTextPrefix + tags;
            tagsTextView.setText(tagsText);
        }

        // Bio text view
        if (currentlyViewedUser.getBiography() == null || currentlyViewedUser.getBiography().isEmpty()) {
            bioTextView.setText(nothingHereText);
        } else {
            String bioTextPrefix = "Biographie:\n";
            String bioText = bioTextPrefix + currentlyViewedUser.getBiography();
            bioTextView.setText(bioText);
        }

        setRandomProfilePicture();
    }

    private void setRandomProfilePicture() {
        PhotoView profilePicture = findViewById(R.id.profilePicture);
        int min = 1;
        int max = 1000000;
        currentlyViewedUser.setProfilePictureSeed(random.nextInt(max - min + 1) + min);
        String url = "https://api.dicebear.com/8.x/lorelei/png?seed=" + currentlyViewedUser.getProfilePictureSeed();

        Glide.with(this)
                .load(url)
                .into(profilePicture);
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);

        // Apply the corresponding text color
        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        TextView uniTextView = findViewById(R.id.UniTextView);
        TextView tagsTextView = findViewById(R.id.TagsTextView);
        TextView bioTextView = findViewById(R.id.BioTextView);
        TextView nameTextView = findViewById(R.id.nameTextView);
        uniTextView.setTextColor(textColor);
        tagsTextView.setTextColor(textColor);
        bioTextView.setTextColor(textColor);
        nameTextView.setTextColor(textColor);
    }
}
