package univie.hci.studymate;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendList.FriendListAdapter adapter;
    private int currentBackgroundIndex = 0;
    private RecyclerView mainLayout;
    private RelativeLayout toolBar;
    private TextView toolbarTitle;
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;
    private User user;
    private NavBar navBar;

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
        setContentView(R.layout.activity_friend_list);

        mainLayout = findViewById(R.id.friend_list_recycler_view);
        toolBar = findViewById(R.id.main_toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        recyclerView = findViewById(R.id.friend_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FriendList friendList = FriendList.getInstance(getApplicationContext());
        List<User> friends = friendList.getFriends();

        adapter = friendList.new FriendListAdapter(friends, this);
        recyclerView.setAdapter(adapter);

        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();

        // setting up Navbar
        user = getUserFromIntent();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_friends);
        navBar = new NavBar(this, bottomNavigationView, user);
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

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainLayout.setBackground(background);
        toolBar.setBackground(background);
        updateTextColor();
    }

    private void updateTextColor() {
        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        toolbarTitle.setTextColor(textColor);
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
