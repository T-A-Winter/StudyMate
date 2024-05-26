package univie.hci.studymate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Comparable<Chat> {
    private List<User> participants;
    private List<Message> messages;

    public Chat() {
        this.participants = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Chat(List<User> participants, List<Message> messages) {
        this.participants = participants;
        this.messages = messages;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    // Method to get the timestamp of the last message
    public LocalDateTime getLastMessageTimestamp() {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1).getDateTime();
        }
        // Return a default timestamp if there are no messages
        return LocalDateTime.MIN;
    }

    @Override
    public int compareTo(Chat other) {
        // Compare chats based on the timestamp of the last message
        return other.getLastMessageTimestamp().compareTo(this.getLastMessageTimestamp());
    }
}
