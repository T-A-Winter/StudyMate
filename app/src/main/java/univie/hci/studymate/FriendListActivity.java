package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendList.FriendListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        recyclerView = findViewById(R.id.friend_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FriendList friendList = FriendList.getInstance(getApplicationContext());
        List<User> friends = friendList.getFriends();

        adapter = friendList.new FriendListAdapter(friends, this);
        recyclerView.setAdapter(adapter);

        ImageView settingsButton = findViewById(R.id.settingsButton);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FriendListActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
