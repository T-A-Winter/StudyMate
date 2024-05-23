package univie.hci.studymate;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CreateAccount extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewPhoto;
    private Spinner universitySpinner;
    private University selectedUniversity;
    private Spinner tagSpinner;
    private EditText nameEditText, emailEditText, phonenumberEditText, specialRequestsEditText;
    private List<Tag> selectedTags = new ArrayList<>();

    //dodano
    private ConstraintLayout mainLayout;
    private int currentBackgroundIndex = 0;
    private int[] backgroundResources = {
            R.drawable.background_gradient,
            R.drawable.background_gradient_other,
            R.drawable.background_gradient_second,
            R.drawable.background_gradient_third

    };
        //pick an image
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
                User user = new User(name, selectedUniversity, selectedTags, email, Integer.parseInt(phoneNumber), specialRequest);
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

        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                try {
                    Tag tag = Tag.valueOf(selected.toUpperCase());
                    if (!selectedTags.contains(tag)) {
                        selectedTags.add(tag);
                    }
                } catch (IllegalArgumentException ex) {
                    Toast.makeText(CreateAccount.this, "Invalid tag selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    //load last saved background index and apply background based on that index
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

        tagSpinner = findViewById(R.id.tagSpinner);
        ArrayAdapter<CharSequence> tagAdapter = ArrayAdapter.createFromResource(this,
                R.array.tag_array, android.R.layout.simple_spinner_item);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        nameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phonenumberEditText = findViewById(R.id.phonenumberEditText);
        specialRequestsEditText = findViewById(R.id.specialRequestsEditText);
        mainLayout = findViewById(R.id.main_layout); //DODANO I OVO

    }
    //User infos to be taken to next view
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
    }

}
