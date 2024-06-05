package univie.hci.studymate;
import java.util.Arrays;
import java.util.Collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private User user;

    private NavBar navBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        TextView userNameTextView = findViewById(R.id.nameTextView);
        TextView userUniversityTextView = findViewById(R.id.UniTextView);
        TextView userBiographyTextView = findViewById(R.id.BioTextView);
        TextView userTagsTextView = findViewById(R.id.TagsTextView);
        ImageView userProfileImageView = findViewById(R.id.profilePicture);

        // Retrieve the user information from the intent
        user = getUserFromIntent();

        // sets up NavBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        navBar = new NavBar(this, bottomNavigationView, user);

        if (user != null) {
            userNameTextView.setText(user.getName());
            userUniversityTextView.setText(user.getUniversity().name());
            userBiographyTextView.setText(user.getBiography());
            // Convert ArrayList<Tag> to Collection<Tag>
            Collection<Tag> tags = new ArrayList<>(user.getTags());
            userTagsTextView.setText(formatTags(tags));
            Glide.with(this).load(user.getProfilePictureUrl()).into(userProfileImageView);
        }
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

    private String formatTags(Collection<Tag> tags) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tags) {
            sb.append(tag.name()).append(", ");
        }
        // Remove the last comma and space
        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
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
