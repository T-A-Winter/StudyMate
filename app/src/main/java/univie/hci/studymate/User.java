package univie.hci.studymate;

import java.util.Collection;
import java.util.Objects;

public class User {
    private String name;
    private University university;
    private Collection<Tag> tags;
    private String email; // Is the ID of the user
    private int phoneNumber;
    private String biography;
    private FriendList friends;
    private ChatList chats;
    /**
     * Base Ctor. <br>
     * This are the least amount of fields a new user needs to fill out before starting.<br>
     * After filling out these 3 fields the "start search" button should appear.
     * */
    public User(String name, University university, Collection<Tag> tags, String email) {
        this.name = name;
        this.university = university;
        this.tags = tags;
        this.email = email;
    }

    /**
     * This Ctor should not be used. Its only for testing and debugging. <br>
     * If a new user is created, it should use User(String, University, Collection<Tag>) Ctor. <br>
     * If a user fills out the other fields, the field should be filled out with the setters.
     * */
    public User(String name, University university, Collection<Tag> tags, String email, int phoneNumber, String biography) {
        this.name = name;
        this.university = university;
        this.tags = tags;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.biography = biography;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void addFriends(User newFriend) {
        this.friends.addFriend(newFriend);
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    /**
     * should open Chat view
     * TODO: IT IS NOT DONE
     * TODO: architecture of chat and how to open chat need to be better implemented!!!!!!!
     * */
    void openChatWith(User user) {
        return;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", university=" + university +
                ", tags=" + tags +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", biography='" + biography + '\'' +
                ", friends=" + friends +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
