package univie.hci.studymate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private List<Message> messages;
    private OnItemClickListener clickListener;

    public ChatListAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.chat_item_layout, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView friendNameTextView;
        private TextView lastMessageTextView;
        private TextView timestampTextView;
        private Message message;
        private ImageView profilePictureImageView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            friendNameTextView = itemView.findViewById(R.id.chat_item_name_text_view);
            lastMessageTextView = itemView.findViewById(R.id.last_message_textview);
            timestampTextView = itemView.findViewById(R.id.timestamp_textview);
            profilePictureImageView = itemView.findViewById(R.id.profilePicture);
            itemView.setOnClickListener(this);
        }

        public void bind(Message message) {
            this.message = message;
            friendNameTextView.setText(message.getFrom().getName());
            lastMessageTextView.setText(message.getMessageContent());
            timestampTextView.setText(message.getTimestamp());
            // Load profile picture into image view
            Glide.with(itemView.getContext())
                    .load(message.getFrom().getProfilePictureUrl())
                    .into(profilePictureImageView);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                clickListener.onItemClick(message);
            }
        }
    }
}
