package univie.hci.studymate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Init a Matching obj. with the current users on the app and call math_one(User) or match_more(User)
 * */
public class Matching {

    private Collection<User> users;


    Matching(Collection<User> users) {
        this.users = users;
    }

    /**
     * @param user The user with the match algo will run on
     * @return the user with the most common Tags. If there are no user, null will be returned
     * */
    public User match_one(User user) {
        return users.stream()
                .filter(other -> !other.equals(user)) // To not return the input user
                .max((userOne, userTwo) -> Integer.compare(
                        countCommonTags(user, userOne),
                        countCommonTags(user, userTwo)))
                .orElse(null);
    }
    /**
     * @param user The user with the match algo will run on
     * @return returns a sorted list of users, sorted by the most common Tag
     * */
    public Collection<User> match_more(User user) {
        return users.stream()
                .filter(other -> !other.equals(user)) // To not return the input user
                .sorted((userOne, userTwo) -> Integer.compare(
                        countCommonTags(user, userTwo),
                        countCommonTags(user, userOne))) // Sort in descending order
                .collect(Collectors.toList());
    }

    private int countCommonTags(User user1, User user2) {
        Set<Tag> user1Tags = new HashSet<>(user1.getTags());
        Set<Tag> user2Tags = new HashSet<>(user2.getTags());

        user1Tags.retainAll(user2Tags);
        return user1Tags.size();
    }
}
