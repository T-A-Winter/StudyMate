package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;

public class CreateAccount extends AppCompatActivity {

    private Spinner universitySpinner;
    private University selectedUniversity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeViews();

        Button startMatchingButton = findViewById(R.id.startMatchingButton);

        // OnClickListener für den Button setzen
        startMatchingButton.setOnClickListener(view -> {
            // Intent erstellen, um zur MatchingActivity zu navigieren
            Intent intent = new Intent(CreateAccount.this, MatchingAlgorithm.class);
            startActivity(intent);
        });

        // Listener, um den ausgewählten Wert zu erfassen
        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                try {
                    selectedUniversity = University.valueOf(selected.replace(" ", "_").toUpperCase());
                } catch (IllegalArgumentException ex) {
                    selectedUniversity = null; // Fallback oder Fehlerbehandlung
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Standardwert setzen oder Fehlerbehandlung
            }



        });

    }

    private void initializeViews() {
        universitySpinner = findViewById(R.id.universitySpinner);
        // Stellen Sie sicher, dass der Spinner mit den Daten aus den Ressourcen gefüllt wird
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.university_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        universitySpinner.setAdapter(adapter);
    }
}
