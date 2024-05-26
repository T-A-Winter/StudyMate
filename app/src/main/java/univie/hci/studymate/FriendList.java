package univie.hci.studymate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendList {
    private static FriendList instance;
    private List<User> friends;

    private FriendList(Context context) {
        friends = new ArrayList<>();

    }

    public static FriendList getInstance(Context context) {
        if (instance == null) {
            instance = new FriendList(context);
        }
        return instance;
    }


    public void addFriend(User user) {
        friends.add(user);
    }

    public void removeFriend(User user) {
        friends.remove(user);
    }

    public List<User> getFriends() {
        return friends;
    }

    public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendViewHolder> {

        private List<User> friends;
        private Context context;
        private FriendList friendListInstance;

        public FriendListAdapter(List<User> friends, Context context) {
            this.friends = friends;
            this.context = context;
            this.friendListInstance = FriendList.getInstance(context);
        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View friendView = inflater.inflate(R.layout.friend_item_layout, parent, false);
            return new FriendViewHolder(friendView);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            User friend = friends.get(position);
            holder.bind(friend);
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        public class FriendViewHolder extends RecyclerView.ViewHolder {

            private TextView friendNameTextView;
            private ImageView chatIcon;
            private ImageView deleteIcon;
            private ImageView infoIcon;

            public FriendViewHolder(@NonNull View itemView) {
                super(itemView);
                friendNameTextView = itemView.findViewById(R.id.friend_name_text_view);
                chatIcon = itemView.findViewById(R.id.chat_icon);
                deleteIcon = itemView.findViewById(R.id.delete_icon);
                infoIcon = itemView.findViewById(R.id.info_icon);
            }

            public void bind(final User friend) {
                friendNameTextView.setText(friend.getName());

                chatIcon.setOnClickListener(v -> {
                    User user = friends.get(getAdapterPosition());
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("userId", user.getId());
                    intent.putExtra("userName", user.getName());
                    context.startActivity(intent);
                });

                deleteIcon.setOnClickListener(v -> {
                    friendListInstance.removeFriend(friend);
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), friends.size());
                });

                infoIcon.setOnClickListener(v -> {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtra("user", friend);
                    context.startActivity(intent);
                });
            }
        }
    }
}
