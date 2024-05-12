package univie.hci.studymate;

import java.util.Collection;

public class FriendList {
    Collection<User> friends;

    /**
     * Needed for debugging. Normally a new user does not start with friends
     * */
    public FriendList(Collection<User> friends) {
        this.friends = friends;
    }

    void addFriend(User user) {
        friends.add(user);
    }
}