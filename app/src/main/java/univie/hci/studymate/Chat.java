package univie.hci.studymate;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
public class Chat {

    private UUID chatId = UUID.randomUUID();
    private Collection<User> chatters;
    private Collection<Message> messages;

    /**
     * placeholder methode. We need to figure out how to show the chats in the view
     * */
    Collection<Message> openChat() {
        return messages;
    }

    public UUID getChatId() {
        return chatId;
    }

    public Collection<User> getChatters() {
        return chatters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(chatId, chat.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                '}';
    }
}
