package univie.hci.studymate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User implements Parcelable {
    private String name;
    private University university;
    private Collection<Tag> tags;
    private String email; // Is the ID of the user
    private Integer phoneNumber = 0;
    private String biography = "";
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

    protected User(Parcel in) {
        name = in.readString();
        university = University.valueOf(in.readString());
        tags = new ArrayList<>();
        in.readList((List<Tag>) tags, Tag.class.getClassLoader());
        email = in.readString();
        phoneNumber = in.readInt();
        biography = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(university.name());
        parcel.writeList(new ArrayList<>(tags));
        parcel.writeString(email);
        parcel.writeInt(phoneNumber);
        parcel.writeString(biography);
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

    public University getUniversity() { return university; }
    public String getBiography() { return biography; }

    /**
     * should open Chat view
     * TODO: IT IS NOT DONE
     * TODO: architecture of chat and how to open chat need to be better implemented!!!!!!!
     * NOTE: Maybe when user scrolls through chatlist, user selects chat, and we than have access to UUID
     * */
    void openChat(UUID chat) {
        chats.openChat(chat);
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
