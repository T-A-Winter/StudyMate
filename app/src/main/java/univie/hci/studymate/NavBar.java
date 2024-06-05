package univie.hci.studymate;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class NavBar {
    private Context context;
    private User user;
    private Map<Integer, Intent> menuIntents;
    static final private String USER_MATCHING_ALGO_STRING = MainActivity.USER_MATCHING_ALGO_STRING;

    public NavBar(Context context, BottomNavigationView bottomNavigationView, User user) {
        this.context = context;
        this.user = user;
        menuIntents = new HashMap<>();
        initializeIntents();
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onMenuItemClick);
    }

    private void initializeIntents() {
        Intent matchingIntent = new Intent(context, MatchingAlgorithm.class);
        matchingIntent.putExtra(USER_MATCHING_ALGO_STRING, user);
        menuIntents.put(R.id.menu_matching, matchingIntent);

        Intent chatsIntent = new Intent(context, ChatListActivity.class);
        chatsIntent.putExtra(USER_MATCHING_ALGO_STRING, user);
        menuIntents.put(R.id.menu_chats, chatsIntent);

        Intent friendsIntent = new Intent(context, FriendListActivity.class);
        friendsIntent.putExtra(USER_MATCHING_ALGO_STRING, user);
        menuIntents.put(R.id.menu_friends, friendsIntent);

        Intent calendarIntent = new Intent(context, CalendarView.class);
        calendarIntent.putExtra(USER_MATCHING_ALGO_STRING, user);
        menuIntents.put(R.id.menu_calendar, calendarIntent);
    }

    boolean onMenuItemClick(@NonNull MenuItem item) {
        Intent intent = menuIntents.get(item.getItemId());
        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
