package univie.hci.studymate;
import java.util.Collection;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

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
        User user = getIntent().getParcelableExtra("user");

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
}
