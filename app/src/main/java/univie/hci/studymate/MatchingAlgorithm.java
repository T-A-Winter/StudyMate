package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MatchingAlgorithm extends AppCompatActivity {
    Random random = new Random();
    // matching algo is created with random users
    Matching matchingAlgo = new Matching(createRandomUsers());
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    User user;
    Queue<User> matchedUsers;
    String nothingHereText = "Sorry nothing here";
    User currentlyViewdUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matching_algorithm);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get the user
        Intent intent = getIntent();
        user = intent.getParcelableExtra(USER_MATCHING_ALGO_STRING);

        // get matching users
        matchedUsers = new LinkedList<>(matchingAlgo.match_more(user));

        // prompt fist matched user
        promptFirstUser();
    }

    private void setDefaultProfilePicture() {
        PhotoView profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setImageResource(R.drawable.defaul_user);
    }

    private Collection<User> createRandomUsers() {
        // creating the users
        return IntStream.range(1, 21)
                .mapToObj(userIndex -> {
                    String name = "User" + userIndex;
                    String email = "user" + userIndex + "@example.com";

                    // rand Uni
                    University randomUniversity = University.values()[random.nextInt(University.values().length)];

                    // selecting 1-3 rand Tags
                    List<Tag> tagList = random.ints(1,4)
                            .mapToObj(n -> Tag.values()[random.nextInt(Tag.values().length)])
                            .distinct()
                            .limit(3)
                            .collect(Collectors.toList());

                    return new User(name, randomUniversity, tagList, email);
                })
                .collect(Collectors.toList());
    }

    private void promptFirstUser() {
        pollUsers();
        promptMatchedUser();
    }

    public void matchedWithUser(View view) {
        user.addFriends(currentlyViewdUser);
        pollUsers();
        promptMatchedUser();
    }

    public void declineWithUser(View view) {
        pollUsers();
        promptMatchedUser();
    }

    private void pollUsers() {
        currentlyViewdUser = matchedUsers.poll();
    }

    private void promptMatchedUser() {
        TextView uniTextView = findViewById(R.id.UniTextView);
        TextView tagsTextView = findViewById(R.id.TagsTextView);
        TextView bioTextView = findViewById(R.id.BioTextView);

        if (currentlyViewdUser == null) {
            uniTextView.setText(nothingHereText);
            tagsTextView.setText(nothingHereText);
            bioTextView.setText(nothingHereText);
            return;
        }
        // get the strings for the TextViews
        uniTextView.setText(currentlyViewdUser.getUniversity().name());
        // Building the tags string TextView
        StringBuilder tags = new StringBuilder();
        boolean first = true;
        for(Tag tag : currentlyViewdUser.getTags()) {
            if (first) {
                first = false;
                tags.append(" ");
            }
            tags.append(tag.name());
        }

        if (currentlyViewdUser.getBiography() == null) {
            bioTextView.setText(nothingHereText);
        }
        bioTextView.setText(currentlyViewdUser.getBiography());

        setRandomProfilePicture();
    }

    private void setRandomProfilePicture() {
        PhotoView profilePicture = findViewById(R.id.profilePicture);
        int min = 1;
        int max = 1000000;
        String url = "https://api.dicebear.com/8.x/lorelei/svg?seed=" + random.nextInt(max - min + 1) + min;

        Glide.with(this)
                .load(url)
                .into(profilePicture);
    }
}