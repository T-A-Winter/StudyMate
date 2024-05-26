package univie.hci.studymate;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatList {
    private static ChatList instance;
    private Map<String, Chat> chatMap;
    private static final String PREFS_NAME = "ChatPrefs";
    private static final String CHATS_KEY = "Chats";
    private Context context;

    private ChatList(Context context) {
        this.context = context;
        chatMap = new HashMap<>();
    }

    public static ChatList getInstance(Context context) {
        if (instance == null) {
            instance = new ChatList(context);
        }
        return instance;
    }

    public Message getLastMessageForUser(String userId) {
        Chat chat = chatMap.get(userId);
        if (chat != null) {
            List<Message> messages = chat.getMessages();
            if (messages != null && !messages.isEmpty()) {
                return messages.get(messages.size() - 1);
            }
        }
        return null;
    }

    public void addChatForUser(String userId, Chat chat) {
        chatMap.put(userId, chat);
        saveChatList();
    }

    public Chat getChatForUser(String userId) {
        return chatMap.get(userId);
    }

    public void saveChatList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(chatMap);
        editor.putString(CHATS_KEY, json);
        editor.apply();
    }

    public void loadChatList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CHATS_KEY, null);
        if (json != null) {
            chatMap = gson.fromJson(json, new TypeToken<Map<String, Chat>>(){}.getType());
        }
    }
}
