package univie.hci.studymate;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ChatList {
    Collection<Chat> chats;

    void addChat(Chat chat) {
        chats.add(chat);
    }

    void openChat(UUID chatId) {

        Optional<Chat> optionalChat =  chats.stream()
                .filter(chat -> chat.getChatId().equals(chatId))
                .findFirst();

        if(!optionalChat.isPresent()){
            throw new RuntimeException("There is no chat with the ID:" + chatId.toString());
        }

        optionalChat.get().openChat();
    }
}
