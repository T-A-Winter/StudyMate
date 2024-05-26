package univie.hci.studymate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> chatMessages;
    private TextView chatUsernameTextView;
    private User currentUser;

    private FriendList getFriendList() {
        return FriendList.getInstance(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageInput = findViewById(R.id.chat_message_input);
        sendButton = findViewById(R.id.message_send_btn);
        chatRecyclerView = findViewById(R.id.chat_recycler_view);
        chatUsernameTextView = findViewById(R.id.chat_username_text_view);

        chatMessages = new ArrayList<>();
        currentUser = getCurrentUser();
        chatAdapter = new ChatAdapter(chatMessages, currentUser.getEmail());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);


        String userName = getIntent().getStringExtra("userName");
        if (userName != null) {
            chatUsernameTextView.setText(userName);
        } else {
            chatUsernameTextView.setText("Username");
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString().trim();
                if (!TextUtils.isEmpty(messageText)) {
                    Message sentMessage = new Message(currentUser, messageText);
                    sendMessage(sentMessage);
                    messageInput.setText("");
                }
            }
        });

        ImageButton btnChatList = findViewById(R.id.btnChatList);
        btnChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, ChatListActivity.class);
                startActivity(intent);
            }
        });

        // Load chat history
        loadChatHistory();
    }

    private void sendMessage(Message message) {
        chatMessages.add(message);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        chatRecyclerView.scrollToPosition(chatMessages.size() - 1);

        saveMessageToChatHistory(message);
    }

    private void loadChatHistory() {
        ChatList chatList = ChatList.getInstance(getApplicationContext());
        Chat chat = chatList.getChatForUser(currentUser.getId());
        if (chat != null) {
            chatMessages.addAll(chat.getMessages());
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(chatMessages.size() - 1);
        }
    }

    private void saveMessageToChatHistory(Message message) {
        ChatList chatList = ChatList.getInstance(getApplicationContext());
        Chat chat = chatList.getChatForUser(currentUser.getId());
        if (chat == null) {
            chat = new Chat();
            chatList.addChatForUser(currentUser.getId(), chat);
        }
        chat.addMessage(message);
        chatList.saveChatList();

    }

    private User getCurrentUser() {
        String userId = getIntent().getStringExtra("userId");
        for (User user : getFriendList().getFriends()) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
