package univie.hci.studymate;

import static univie.hci.studymate.MainActivity.USER_MATCHING_ALGO_STRING;

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

public class ChatListActivity extends AppCompatActivity implements ChatListAdapter.OnItemClickListener {
    //next 3 values are for vote
    private ArrayList<String> timeSlots = null;
    private String eventTitle = null;
    private String eventDate = null;

    private RecyclerView chatListRecyclerView;
    private ChatListAdapter chatListAdapter;
    private RelativeLayout mainToolbar;
    private RecyclerView chatList;
    private int currentBackgroundIndex = 0;
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
        setContentView(R.layout.activity_chat_list);

        //needed for calendar vote
        timeSlots = getIntent().getStringArrayListExtra("timeSlots");
        eventTitle = getIntent().getStringExtra("title");
        eventDate = getIntent().getStringExtra("date");

        // for color
        mainToolbar = findViewById(R.id.main_toolbar);
        chatList = findViewById(R.id.chat_list);
        currentBackgroundIndex = getSharedPreferences("prefs", MODE_PRIVATE).getInt("backgroundIndex", 0);
        applyBackground();

        chatListRecyclerView = findViewById(R.id.chat_list);
        chatListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FriendList friendList = FriendList.getInstance(this);

        List<Message> messagesWithChatHistory = filterMessagesWithChatHistory(friendList.getFriends());

        chatListAdapter = new ChatListAdapter(messagesWithChatHistory);
        chatListAdapter.setOnItemClickListener(this);
        chatListRecyclerView.setAdapter(chatListAdapter);

        // setting up Navbar
        user = getUserFromIntent();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_chats);
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

    private List<Message> filterMessagesWithChatHistory(List<User> allUsers) {
        List<Message> messagesWithChatHistory = new ArrayList<>();
        ChatList chatList = ChatList.getInstance(this);

        for (User user : allUsers) {
            Message lastMessage = chatList.getLastMessageForUser(user.getId());
            if (lastMessage != null) {
                messagesWithChatHistory.add(lastMessage);
            }
        }

        return messagesWithChatHistory;
    }

    @Override
    public void onItemClick(Message message) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("userId", message.getFrom().getId());
        intent.putExtra("userName", message.getFrom().getName());
        if (timeSlots != null) intent.putStringArrayListExtra("timeSlots", timeSlots);
        if (eventTitle != null)intent.putExtra("title", eventTitle);
        if (eventDate != null)intent.putExtra("date", eventDate);
        startActivity(intent);
    }

    private void applyBackground() {
        Drawable background = ContextCompat.getDrawable(this, backgroundResources[currentBackgroundIndex]);
        mainToolbar.setBackground(background);
        chatList.setBackground(background);

        // Apply the corresponding text color
        int textColor = ContextCompat.getColor(this, textColors[currentBackgroundIndex]);
        // Assuming you have a TextView in the main_toolbar, set its color here
        TextView toolbarTitle = mainToolbar.findViewById(R.id.toolbar_title); // Adjust this ID as necessary
        toolbarTitle.setTextColor(textColor);
    }
}
