package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    public static final String USER_MATCHING_ALGO_STRING = "USER_MATCHING_ALGO_STRING";
    private Button createAccountButton ;


    private Button loginEmailButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        createAccountButton = findViewById(R.id.createAccountButton);
        loginEmailButton = findViewById(R.id.loginEmailButton);

      /*  ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(intent);
            }


        });

        loginEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginEmail.class);
                startActivity(intent);
            }
        });




        // TODO: Comment this out for now. Otherwise it starts the search/matching algo
       // startSearch();
    }

        //TODO: eventuell löschen weil man aus CreateAccount.java auf MatchingAlgorithm zugreifen kann
    private void startSearch() {
        Intent intent = new Intent(MainActivity.this, MatchingAlgorithm.class);
        // TODO: When the user is created put it in the intent as a extra
        // User user; TODO: get the infos for a new user and create the user
        // intent.putExtra(USER_MATCHING_ALGO_STRING, user);
        startActivity(intent);
    }


}