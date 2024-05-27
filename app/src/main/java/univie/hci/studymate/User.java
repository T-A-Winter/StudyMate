package univie.hci.studymate;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class User implements Parcelable {
    private String name;
    private University university;
    private Collection<Tag> tags;
    private String email; // Is the ID of the user
    private Integer phoneNumber = 0;
    private String biography = "";
    //private FriendList friends = new FriendList(); // Removed direct instantiation
    private int profilePictureSeed = 0;
    public String getProfilePictureUrl() {

        return "https://api.dicebear.com/8.x/lorelei/png?seed=" + profilePictureSeed;
    }
    public User(String name, University university, Collection<Tag> tags, String email) {
        this.name = name;
        this.university = university;
        this.tags = tags;
        this.email = email;
    }

    public void setProfilePictureSeed(int profilePictureSeed) {
        this.profilePictureSeed = profilePictureSeed;
    }

    public int getProfilePictureSeed() {
        return profilePictureSeed;
    }

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
        profilePictureSeed = in.readInt();
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
        parcel.writeInt(profilePictureSeed);
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return name; // Assuming name is user ID
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public University getUniversity() { return university; }
    public String getBiography() { return biography; }
    public String getName() { return name; }
    public String getEmail() { return email; }

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
