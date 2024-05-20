package univie.hci.studymate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class CreateAccount extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewPhoto;
    private Spinner universitySpinner;
    private University selectedUniversity;
    private Spinner tagSpinner;

    // ActivityResultLauncher für das Ergebnis nach Auswahl des Bildes
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

        addPhotoButton.setOnClickListener(v -> openGallery());

        Button startMatchingButton = findViewById(R.id.startMatchingButton);
        startMatchingButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAccount.this, MatchingAlgorithm.class);
            startActivity(intent);
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
            }
        });

        tagSpinner = findViewById(R.id.tagSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tag_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(adapter);
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
    }
}
