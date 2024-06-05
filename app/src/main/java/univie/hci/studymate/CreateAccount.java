package univie.hci.studymate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CreateAccount extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewPhoto;
    private Spinner universitySpinner;
    private University selectedUniversity;
    private EditText nameEditText, emailEditText, phonenumberEditText, specialRequestsEditText, tagsEditText;
    private List<Tag> selectedTags = new ArrayList<>();

    // Additions
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

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    Glide.with(this).load(imageUri).into(imageViewPhoto);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeViews();

        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        Button addPhotoButton = findViewById(R.id.addPhotoButton);
        Button startMatchingButton = findViewById(R.id.startMatchingButton);

        addPhotoButton.setOnClickListener(v -> openGallery());

        startMatchingButton.setOnClickListener(view -> {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phoneNumber = phonenumberEditText.getText().toString();
            String specialRequest = specialRequestsEditText.getText().toString();

            if (name.isEmpty() || email.isEmpty() || selectedUniversity == null || selectedTags.isEmpty()) {
                Toast.makeText(CreateAccount.this, "Please fill all required fields: Name, Email, University, and at least one Tag", Toast.LENGTH_SHORT).show();
            } else {
                int phoneNumberInt = phoneNumber.isEmpty() ? 0 : Integer.parseInt(phoneNumber);
                User user = new User(name, selectedUniversity, selectedTags, email, phoneNumberInt, specialRequest);
                startSearch(user);
            }
        });

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                try {
                    selectedUniversity = University.valueOf(selected.replace(" ", "_").toUpperCase());
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(CreateAccount.this, "Invalid university selected", Toast.LENGTH_SHORT).show();
                    selectedUniversity = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedUniversity = null;
            }
        });

        tagsEditText.setOnClickListener(v -> showTagDialog());

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mGetContent.launch(intent);
    }

    private void initializeViews() {
        universitySpinner = findViewById(R.id.universitySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.university_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(adapter);

        nameEditText = findViewById(R.id.UsernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phonenumberEditText = findViewById(R.id.phonenumberEditText);
        specialRequestsEditText = findViewById(R.id.specialRequestsEditText);
        tagsEditText = findViewById(R.id.tagsEditText);
        mainLayout = findViewById(R.id.main_layout);
    }

    private void showTagDialog() {
        boolean[] checkedTags = new boolean[Tag.values().length];
        String[] tagNames = new String[Tag.values().length];
        for (int i = 0; i < Tag.values().length; i++) {
            tagNames[i] = Tag.values()[i].name();
            checkedTags[i] = selectedTags.contains(Tag.values()[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Tags")
                .setMultiChoiceItems(tagNames, checkedTags, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        selectedTags.add(Tag.values()[which]);
                    } else {
                        selectedTags.remove(Tag.values()[which]);
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> updateTagsEditText())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void updateTagsEditText() {
        StringBuilder selectedTagsString = new StringBuilder();
        for (Tag tag : selectedTags) {
            if (selectedTagsString.length() > 0) {
                selectedTagsString.append(", ");
            }
            selectedTagsString.append(tag.name());
        }
        tagsEditText.setText(selectedTagsString.toString());
    }

    private void startSearch(User user) {
        Intent intent = new Intent(CreateAccount.this, MatchingAlgorithm.class);
        intent.putExtra(MainActivity.USER_MATCHING_ALGO_STRING, user);
        startActivity(intent);
    }

    public void backButton(View view) {
        finish();
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);

        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        nameEditText.setTextColor(textColor);
        emailEditText.setTextColor(textColor);
        phonenumberEditText.setTextColor(textColor);
        specialRequestsEditText.setTextColor(textColor);
        tagsEditText.setTextColor(textColor);
    }
}
