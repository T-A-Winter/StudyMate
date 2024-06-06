package univie.hci.studymate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SENT_TIME_SLOT = 3;

    private List<Message> chatMessages;
    private String currentUserEmail;

    public ChatAdapter(List<Message> chatMessages, String currentUserEmail) {
        this.chatMessages = chatMessages;
        this.currentUserEmail = currentUserEmail;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = chatMessages.get(position);
        if (message.getFrom().getEmail().equals(currentUserEmail)) {
            if (message.getVote()){
                return VIEW_TYPE_SENT_TIME_SLOT;
            }
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case VIEW_TYPE_SENT:
                view = inflater.inflate(R.layout.chat_message_sent, parent, false);
                return new SentMessageViewHolder(view);
            case VIEW_TYPE_RECEIVED:
                view = inflater.inflate(R.layout.chat_message_received, parent, false);
                return new ReceivedMessageViewHolder(view);
            case VIEW_TYPE_SENT_TIME_SLOT:
                view = inflater.inflate(R.layout.chat_vote_sent, parent, false);
                return new SentTimeSlotViewHolder(view);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = chatMessages.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else if (holder.getItemViewType() == VIEW_TYPE_SENT_TIME_SLOT) {
            ((SentTimeSlotViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    private static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView sentMessageTextView;
        private TextView sentMessageTimestamp;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMessageTextView = itemView.findViewById(R.id.right_chat_textview);
            sentMessageTimestamp = itemView.findViewById(R.id.right_chat_timestamp);
        }

        public void bind(Message message) {
            sentMessageTextView.setText(message.getMessageContent());
            sentMessageTimestamp.setText(message.getTimestamp());
        }
    }

    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView receivedMessageTextView;
        private TextView receivedMessageTimestamp;
        private ImageView profilePicture;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            receivedMessageTextView = itemView.findViewById(R.id.left_chat_textview);
            receivedMessageTimestamp = itemView.findViewById(R.id.left_chat_timestamp);
            profilePicture = itemView.findViewById(R.id.profilePicture);
        }

        public void bind(Message message) {
            receivedMessageTextView.setText(message.getMessageContent());
            receivedMessageTimestamp.setText(message.getTimestamp());

            // loading profile picture into image view
            User user = message.getFrom();
            if (user != null && user.getProfilePictureUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(user.getProfilePictureUrl())
                        .into(profilePicture);
            }
        }
    }

    private static class SentTimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView sentTimeSlotTextView, sentTimeSlotTimestamp;

        public SentTimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            sentTimeSlotTextView = itemView.findViewById(R.id.sent_time_slot_textview);
            sentTimeSlotTimestamp = itemView.findViewById(R.id.right_chat_timestamp);
        }

        public void bind(Message message) {
            sentTimeSlotTextView.setText(message.getMessageContent());
            sentTimeSlotTimestamp.setText(message.getTimestamp());
        }
    }
}
