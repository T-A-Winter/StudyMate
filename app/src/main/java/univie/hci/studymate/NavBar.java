package univie.hci.studymate;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class NavBar {
    private Context context;
    private Map<Integer, Intent> menuIntents;

    public NavBar(Context context) {
        this.context = context;
        menuIntents = new HashMap<>();
        initializeIntents();
    }

    private void initializeIntents() {
        menuIntents.put(R.id.menu_matching, new Intent(context, MatchingAlgorithm.class));
        menuIntents.put(R.id.menu_chats, new Intent(context, ChatListActivity.class));
        menuIntents.put(R.id.menu_friends, new Intent(context, FriendListActivity.class));
        menuIntents.put(R.id.menu_calendar, new Intent(context, CalendarView.class));
    }

    public boolean onMenuItemClick(@NonNull MenuItem item) {
        Intent intent = menuIntents.get(item.getItemId());
        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}